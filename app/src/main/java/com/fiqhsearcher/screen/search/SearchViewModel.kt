package com.fiqhsearcher.screen.search

import android.content.Context
import androidx.lifecycle.ViewModel
import com.fiqhsearcher.fiqh.Fiqh
import com.fiqhsearcher.fiqh.Madhab
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

val DummyResults = (1..20).map {
    val name = it.toString().repeat(20)
    SearchResult(name, name, name)
}

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    fun search() = flowOf(DummyResults)

    fun search(
        context: Context,
        query: String,
        madhab: Madhab = Madhab.HANBALI,
        fiqh: Fiqh = Fiqh.IBADAT
    ): Flow<State<List<SearchResult>>> {
        return flow<State<List<SearchResult>>> {
//            val snapshot = database.child("").get().await()
//            val posts = snapshot.toObjects(SearchResult::class.java)
//
//             Emit success state with data
//            emit(State.success(posts))
        }.catch {
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

//    val Madhab.collection get() = Firebase.firestore.collection(name.lowercase())

}

@JsonClass(generateAdapter = true)
data class SearchResult(
    @Json(name = "q") val question: String = "",
    @Json(name = "proof") val proof: String = "",
    @Json(name = "a") val answer: String = ""
)