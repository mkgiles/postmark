package net.mkgiles.postmark.models

import android.content.Context
import android.text.format.DateFormat
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import net.mkgiles.postmark.helpers.exists
import net.mkgiles.postmark.helpers.read
import net.mkgiles.postmark.helpers.write
import java.lang.reflect.Type
import java.util.*

class PackageJSONStore(private val context: Context) : PackageStore {
    private var parcels : MutableList<PackageModel> = mutableListOf()
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(object: JsonAdapter<Date>(){
            @FromJson
            override fun fromJson(reader: JsonReader): Date? {
                return DateFormat.getDateFormat(context).parse(reader.nextString())
            }
            @ToJson
            override fun toJson(writer: JsonWriter, value: Date?) {
                value?.let{
                    writer.value(DateFormat.getDateFormat(context).format(it))
                }
            }
        })
        .build()
    private val type : Type = Types.newParameterizedType(List::class.java, PackageModel::class.java)
    private val adapter: JsonAdapter<List<PackageModel>> = moshi.adapter(type)
    init {
        if(exists(context, "packages.json"))
            deserialise()
    }
    override fun findAll(): List<PackageModel> {
        return parcels
    }

    override fun find(uid: Long): PackageModel? {
        return parcels.find{it.uid == uid}
    }

    override fun add(parcel: PackageModel) {
        parcels.add(parcel)
        serialise()
    }

    override fun update(parcel: PackageModel) {
        parcels.find{found ->  found.uid == parcel.uid}?.let { found ->
            found.carrier = parcel.carrier
            found.delivered = parcel.delivered
            found.id = parcel.id
            found.name = parcel.name
            found.updated = parcel.updated
        }
        serialise()
    }

    override fun delete(parcel: PackageModel) {
        parcels.remove(parcel)
        serialise()
    }

    private fun serialise(){
        val json = adapter.toJson(parcels)
        write(context, "packages.json", json)
    }
    private fun deserialise(){
        val json = read(context, "packages.json")
        adapter.fromJson(json)?.let{
            parcels = it.toMutableList()
        }
    }

}

