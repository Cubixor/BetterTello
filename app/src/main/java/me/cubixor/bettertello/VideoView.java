package me.cubixor.bettertello;

import android.graphics.Point;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import me.cubixor.telloapi.api.listeners.VideoListener;
import me.cubixor.telloapi.utils.ByteUtils;


public class VideoView implements VideoListener/*, MediaPlayer.OnPreparedListener*/ {

    private final int height = 720;
    MediaCodec codec;
    ByteArrayOutputStream frameData = new ByteArrayOutputStream();
    SurfaceView view;
    //MediaPlayer mediaPlayer;
    boolean surfaceReady;
    boolean surfaceDestroyed;
    private byte[] sps/* = new byte[]{0, 0, 0, 1, 103, 77, 64, 40, -107, -96, 60, 5, -71}*/;
    private byte[] pps/* = new byte[]{0, 0, 0, 1, 104, -18, 56, -128}*/;
    private int width;

    public VideoView() {
        view = MainActivity.getActivity().findViewById(R.id.videoView);


        view.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                surfaceReady = true;
                surfaceDestroyed = false;

                 /* mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDisplay(view.getHolder());
                    mediaPlayer.setDataSource("/sdcard/Download/Test.mp4");
                    mediaPlayer.setVolume(0,0);
                    mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                    mediaPlayer.prepare();
                    mediaPlayer.setOnPreparedListener(instance);*/
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                surfaceDestroyed = true;
            }
        });

    }


/*
    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }
*/


    private void init() {
        if (!surfaceReady) {
            return;
        }

        setViewSize();

        MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
        format.setByteBuffer("csd-0", ByteBuffer.wrap(sps));
        format.setByteBuffer("csd-1", ByteBuffer.wrap(pps));

        if (codec != null) {
            codec.stop();
        }

        try {
            String str = format.getString("mime");
            codec = MediaCodec.createDecoderByType(str);
            codec.configure(format, view.getHolder().getSurface(), null, 0);
            codec.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            codec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainActivity.getActivity().runOnUiThread(() ->
                view.setForeground(null));
    }

    private void setViewSize() {
        if (sps.length == 14) {
            width = 1280;
        } else {
            width = 960;
        }

        MainActivity.getActivity().runOnUiThread(() ->
        {
            float videoProportion = (float) width / (float) height;

            Point size = new Point();
            MainActivity.getActivity().getWindowManager().getDefaultDisplay().getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;
            float screenProportion = (float) screenWidth / (float) screenHeight;

            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (videoProportion > screenProportion) {
                lp.width = screenWidth;
                lp.height = (int) ((float) screenWidth / videoProportion);
            } else {
                lp.width = (int) (videoProportion * (float) screenHeight);
                lp.height = screenHeight;
            }

            view.setLayoutParams(lp);
        });

    }

    @Override
    public void onVideoDataReceived(byte[] data) {
        if (surfaceDestroyed) {
            return;
        }


        byte[] dataTrimmed = ByteUtils.trim(data);
        byte[] dataNoTick = Arrays.copyOfRange(dataTrimmed, 2, dataTrimmed.length);

        int tick = ByteBuffer.wrap(dataTrimmed, 0, 2).getShort() & 0xffff;
        //int nalType = data[6] & 0x1f;


        //System.out.println("VIDEODATA:   LEN: " + dataTrimmed.length + "   TICK: " + tick + "   NALTYPE: " + nalType + "   DATA: " + Utils.bytesToHex(dataTrimmed));

        if (dataNoTick.length < 15) {
            //System.out.println("SPS/PPS:   LEN: " + dataTrimmed.length + "   TICK: " + tick + "   DATA:" + Utils.bytesToHex(dataTrimmed));


            if (dataNoTick.length == 8) {
                //System.out.println("PPS: " + Utils.bytesToHex(dataNoTick));
                pps = dataNoTick;
            } else if (dataNoTick.length == 14 || dataNoTick.length == 13) {
                //System.out.println("SPS: " + Utils.bytesToHex(dataNoTick));
                int oldLen = sps == null ? -1 : sps.length;
                sps = dataNoTick;

                if (codec != null && pps != null && oldLen != -1 && oldLen != sps.length) {
                    init();
                }
            }

            if (codec == null && sps != null && pps != null) {
                init();
            }

            frameData.reset();
            return;
        }

        if (codec == null) {
            return;
        }

/*
        if(nalType == 7 || nalType == 8){
            return;
        }
*/

        if (data[2] == 0x00 && data[3] == 0x00 && data[4] == 0x00 && data[5] == 0x01) {

            byte[] frameBytes = ByteUtils.trim(frameData.toByteArray());

            if (frameBytes.length == 0) {
                try {
                    frameData.write(dataNoTick);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            //System.out.println("FULLFRAME:   LEN:" + frameBytes.length + "   DATA: " + Utils.bytesToHex(frameBytes));


            int inputIndex = 0;
            try {
                inputIndex = codec.dequeueInputBuffer(-1);// Pass in -1 here as in this example we don't have a playback time reference
            } catch (Exception e) {
                e.printStackTrace();
            }


            // If  the buffer number is valid use the buffer with that index
            if (inputIndex >= 0) {
                ByteBuffer buffer = codec.getInputBuffer(inputIndex);
                buffer.clear();
                buffer.put(frameBytes);
                // tell the decoder to process the frame
                codec.queueInputBuffer(inputIndex, 0, frameBytes.length, 0, 0);
            }

            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            int outputIndex = codec.dequeueOutputBuffer(info, 0);
            while (outputIndex >= 0) {
                codec.releaseOutputBuffer(outputIndex, true);

                outputIndex = codec.dequeueOutputBuffer(info, 0L);
            }


            frameData.reset();
        }

        try {
            frameData.write(dataNoTick);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
