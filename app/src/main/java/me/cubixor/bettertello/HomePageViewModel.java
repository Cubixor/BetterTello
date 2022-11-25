package me.cubixor.bettertello;

import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;


import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class HomePageViewModel extends ViewModel {

    private MutableLiveData<List<TextView>> stateBars;

    public LiveData<List<TextView>> getStateBars(){
        if(stateBars == null){
            stateBars = new MutableLiveData<>();
        }

        return stateBars;
    }

    private MutableLiveData<Boolean> statesExpanded;
    public LiveData<Boolean> getStatesExpanded(){
        if(statesExpanded == null){
            statesExpanded = new MutableLiveData<>();
        }
        return statesExpanded;

    }

}
