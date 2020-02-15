package net.mkgiles.postmark.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.mkgiles.postmark.models.PackageModel

class HomeViewModel : ViewModel() {
    val list : MutableLiveData<MutableList<PackageModel>> = MutableLiveData(mutableListOf())
}