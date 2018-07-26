package com.base.openeye

import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test1() {
        val list = listOf("haha", "1", "2", "3")
        list
                .toObservable()
                .subscribeBy(
                        onNext = {
                            println(it)
                        },
                        onComplete = {
                            println("------完成------")
                        },
                        onError = {
                            it.printStackTrace()
                        }
                )


    }
}
