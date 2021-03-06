package app.web.drjackycv.presentation.base.viewmodel

import android.content.res.Resources
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.web.drjackycv.domain.base.Failure
import app.web.drjackycv.presentation.R
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.FlowableSubscribeProxy
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

open class BaseViewModel @ViewModelInject constructor() : ViewModel(),
    LifecycleScopeProvider<BaseViewModel.ViewModelEvent> {

    private val lifecycleEventsDelegate =
        lazy { BehaviorSubject.createDefault(ViewModelEvent.CREATED) }

    private val lifecycleEvents by lifecycleEventsDelegate

    @Inject
    lateinit var resources: Resources

    private var mutableFailure: MutableLiveData<Failure> = MutableLiveData()
    val ldFailure: LiveData<Failure> = mutableFailure

    override fun lifecycle(): Observable<ViewModelEvent> {
        return lifecycleEvents.hide()
    }

    override fun correspondingEvents(): CorrespondingEventsFunction<ViewModelEvent>? {
        return CORRESPONDING_EVENTS
    }

    override fun peekLifecycle(): ViewModelEvent? {
        return lifecycleEvents.value
    }

    override fun onCleared() {
        if (lifecycleEventsDelegate.isInitialized()) {
            lifecycleEvents.onNext(ViewModelEvent.CLEARED)
        }
        super.onCleared()
    }

    fun handleFailure(throwable: Throwable, retryAction: () -> Unit) {
        val failure = throwable as? Failure
            ?: Failure.FailureWithMessage(resources.getString(R.string.something_went_wrong))

        failure.retryAction = retryAction
        mutableFailure.value = failure
    }

    @CheckReturnValue
    fun <T> Flowable<T>.autoDisposable(): FlowableSubscribeProxy<T> =
        this.`as`(AutoDispose.autoDisposable(this@BaseViewModel))


    enum class ViewModelEvent {
        CREATED, CLEARED
    }

    companion object {
        private val CORRESPONDING_EVENTS = CorrespondingEventsFunction<ViewModelEvent> { event ->
            when (event) {
                ViewModelEvent.CREATED -> ViewModelEvent.CLEARED
                else -> throw LifecycleEndedException(
                    "Cannot bind to ViewModel lifecycle after onCleared."
                )
            }
        }
    }

}