package net.mkgiles.postmark.helpers

import android.content.Context
import android.util.Log
import java.io.*

fun write(context: Context, fileName: String, data: String){
    try {
        val out = OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
        out.write(data)
        out.close()
    }catch(e: Exception){
        Log.e("Error: ", "Cannot read file: " + e.toString())
    }
}

fun read(context: Context, fileName: String): String {
    var str = ""
    try {
        val ins = context.openFileInput(fileName)
        val builder = StringBuilder()
        ins?.let{
            val reader = BufferedReader(InputStreamReader(it))
            var done = false
            while(!done){
                val line = reader.readLine()
                done = (line == null)
                if(line!=null) builder.append(line)
            }
        }
        ins.close()
        str = builder.toString()
    } catch(e: FileNotFoundException){
        Log.e("Error: ", "File Not Found: " + e.toString())
    } catch(e: IOException){
        Log.e("Error: ", "Cannot Read File: " + e.toString())
    }
    return str
}

fun exists(context: Context, fileName: String): Boolean {
    val file = context.getFileStreamPath(fileName)
    return file.exists()
}