package me.cubixor.bettertello;

import android.app.Activity;
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


//TODO Cleanup
public class VideoView implements VideoListener {

    private final Activity activity;
    private final SurfaceView view;
    private final ByteArrayOutputStream frameData;
    private final int height = 720;
    private int width;
    boolean surfaceReady = false;
    //boolean surfaceDestroyed;
    private MediaCodec codec;
    private byte[] sps/* = new byte[]{0, 0, 0, 1, 103, 77, 64, 40, -107, -96, 60, 5, -71}*/;
    private byte[] pps/* = new byte[]{0, 0, 0, 1, 104, -18, 56, -128}*/;


    public VideoView(Activity activity, SurfaceView view) {
        this.activity = activity;
        this.view = view;
        frameData = new ByteArrayOutputStream();

        view.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                surfaceReady = true;
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                surfaceReady = false;
                if (codec != null) {
                    codec.stop();
                    codec = null;
                }
            }
        });

    }

    private void init() {
        if (!surfaceReady) {
            return;
        }

        setViewSize();

        MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
        format.setByteBuffer("csd-0", ByteBuffer.wrap(sps));
        format.setByteBuffer("csd-1", ByteBuffer.wrap(pps));

        try {
            if (codec == null) {
                String type = format.getString(MediaFormat.KEY_MIME);
                codec = MediaCodec.createDecoderByType(type);
                codec.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            } else {
                codec.stop();
            }

            codec.configure(format, view.getHolder().getSurface(), null, 0);
            codec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        activity.runOnUiThread(() -> view.setForeground(null));
    }

    private void setViewSize() {
        if (sps.length == 14) {
            width = 1280;
        } else {
            width = 960;
        }

        float videoProportion = (float) width / (float) height;

        activity.runOnUiThread(() ->
        {

            Point size = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(size);
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
        if (!surfaceReady) {
            return;
        }

        byte[] dataTrimmed = ByteUtils.trim(data);
        byte[] dataNoTick = Arrays.copyOfRange(dataTrimmed, 2, dataTrimmed.length);

        //int tick = ByteBuffer.wrap(dataTrimmed, 0, 2).getShort() & 0xffff;
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

        /*if(nalType == 7 || nalType == 8){
            return;
        }*/

        if (data[2] == 0x00 && data[3] == 0x00 && data[4] == 0x00 && data[5] == 0x01) {

            if (frameData.size() == 0) {
                try {
                    frameData.write(dataNoTick);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            byte[] frameBytes = ByteUtils.trim(frameData.toByteArray());

            //System.out.println("FULLFRAME:   LEN:" + frameBytes.length + "   DATA: " + Utils.bytesToHex(frameBytes));


            int inputIndex = 0;
            try {
                inputIndex = codec.dequeueInputBuffer(-1);// Pass in -1 here as in this example we don't have a playback time reference
            } catch (Exception e) {
                e.printStackTrace();
            }


            // If  the buffer number is valid use the buffer with that index
            if (inputIndex >= 0) {
                codec.getInputBuffer(inputIndex).put(frameBytes);
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
