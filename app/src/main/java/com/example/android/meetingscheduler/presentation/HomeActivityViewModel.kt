package com.example.android.meetingscheduler.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.meetingscheduler.entities.SchedulingEntity
import com.example.android.meetingscheduler.remote.RemoteDataFetchClass
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * RxJava implementation skeleton for HomeActivity
 */
class HomeActivityViewModel : ViewModel() {

    private val listOfEvents = MutableLiveData<List<SchedulingEntity>>()

    private val errorLiveData: LiveData<String> by lazy { MutableLiveData<String>() }

    private var disposale: Disposable? = null

    fun initialize(editTextDate: String) {
        disposale = Single.just(RemoteDataFetchClass()).map {
            it.execute()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {

                }, {
            (errorLiveData as MutableLiveData).value = it.localizedMessage
        }
        )

    }

    fun onSuccess(schedulingEvents: ArrayList<SchedulingEntity>) {
        disposale = Single.just(schedulingEvents).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    listOfEvents.value = it
                }, {
            (errorLiveData as MutableLiveData).value = it.localizedMessage
        }
        )
    }

    override fun onCleared() {
        disposale?.dispose()
        super.onCleared()
    }

}