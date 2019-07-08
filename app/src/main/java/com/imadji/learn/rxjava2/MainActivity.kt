package com.imadji.learn.rxjava2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DefaultObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disposable = Observable.fromArray(1, 2, 3, 4, 5)
                .doOnNext {
                    if (it == 3) {
                        throw Throwable("Exception on 3.")
                    }
                }
                .doOnError { Log.d("Log-Rx", "Intercept Error => ${it.message}") }
                .subscribeOn(Schedulers.io())
                .subscribeWith(getObserver())
    }

    private fun getObserver(): DisposableObserver<Int> {
        return object : DisposableObserver<Int>() {
            override fun onComplete() {
                Log.d("Log-Rx", "Completed")
            }

            override fun onNext(t: Int) {
                Log.d("Log-Rx", t.toString())
            }

            override fun onError(e: Throwable) {
                Log.d("Log-Rx", e.message)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
