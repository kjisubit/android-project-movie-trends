package com.js.movietrends.helper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

// viewModelScope는 Dispatchers.Main을 사용한다.
// Dispatchers.Main은 (메인 스레드의 작업 큐를 관리하는) 메인 루퍼에 코루틴 작업을 전달하는 역할을 수행한다.
// 그러나 단위 테스트 환경에는 메인 루퍼가 없어 Dispatchers.Main이 초기화되지 않는다.
// setMain을 사용해 UnconfinedTestDispatcher를 메인 디스페처로 지정하여 viewModelScope.launch가 동작하게 한다.
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) = Dispatchers.setMain(testDispatcher)
    override fun finished(description: Description) = Dispatchers.resetMain()
}
