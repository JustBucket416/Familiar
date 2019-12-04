package justbucket.familiar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import justbucket.familiar.resource.Resource

/**
 * @author JustBucket on 2019-07-15
 */
open class BaseViewModel<Data> : ViewModel() {

    val liveData = MutableLiveData<Resource<Data>>()
}