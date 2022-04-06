package com.fiqhsearcher.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiqhsearcher.data.Issue
import com.fiqhsearcher.data.Section
import com.fiqhsearcher.data.Topic
import com.fiqhsearcher.fiqh.Madhhab
import com.fiqhsearcher.screen.home.SearchOption
import com.fiqhsearcher.supabase.SupabaseClient
import dagger.hilt.android.lifecycle.HiltViewModel
import io.supabase.postgrest.builder.SearchType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@Suppress("RemoveExplicitTypeArguments")
class SupabaseViewModel @Inject constructor(
    val client: SupabaseClient
) : ViewModel() {

    fun getSections(madhhab: Madhhab = Madhhab.HANBALI) = flow {
        emit(
            client.data.from<Section>("sections")
                .select("*")
                .eq("madhhab", madhhab.name.lowercase())
                .execute()
                .getAs<List<Section>>()
        )
    }.flowOn(Dispatchers.IO)

    fun search(searchOption: SearchOption, query: String, madhhab: Madhhab) = flow {
        emit(
            client.data.from<Issue>("issues")
                .select("*")
                .eq("madhhab", madhhab.dataName)
                .textSearch(
                    column = searchOption.column,
                    query = query,
                    type = SearchType.WEB_SEARCH
                ).execute()
                .getAs<List<Issue>>()
        )
    }.flowOn(Dispatchers.IO)


    fun getTopics(madhhab: Madhhab, id: Int) = flow {
        emit(
            client.data.from<Topic>("topics")
                .select("*")
                .eq("madhhab", madhhab.dataName)
                .eq("section", id)
                .execute()
                .getAs<List<Topic>>()
        )
    }.flowOn(Dispatchers.IO)

    fun getIssues(madhhab: Madhhab, section: Int, topic: Int) = flow {
        emit(
            client.data.from<Topic>("issues")
                .select("*")
                .eq("madhhab", madhhab.dataName)
                .eq("section", section)
                .eq("topic", topic)
                .execute()
                .getAs<List<Issue>>()
        )
    }.flowOn(Dispatchers.IO)

    fun createTopic(section: Int, madhhab: Madhhab, name: String): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            client.data.from<Topic>("topics")
                .insert(Topic(name = name, madhhab = madhhab, section = section))
                .execute().response.body!!.string().also { println(it) }
        }
    }

    fun createSection(madhhab: Madhhab, name: String): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            client.data.from<Section>("sections")
                .insert(Section(name = name, madhhab = madhhab))
                .execute().response.body!!.string().also { println(it) }
        }
    }

    fun createIssue(
        madhhab: Madhhab,
        answer: String,
        topic: Int,
        section: Int,
        proof: String,
        question: String
    ): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            client.data.from<Issue>("issues")
                .insert(
                    Issue(
                        question = question,
                        answer = answer,
                        proof = proof,
                        madhhab = madhhab,
                        section = section,
                        topic = topic
                    )
                )
                .execute().response.body!!.string().also { println(it) }
        }
    }
}
