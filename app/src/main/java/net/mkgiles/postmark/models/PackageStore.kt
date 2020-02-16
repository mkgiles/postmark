package net.mkgiles.postmark.models

interface PackageStore {
    fun findAll() : List<PackageModel>
    fun find(uid: Long) : PackageModel?
    fun add(parcel: PackageModel)
    fun update(parcel: PackageModel)
    fun delete(parcel: PackageModel)
}