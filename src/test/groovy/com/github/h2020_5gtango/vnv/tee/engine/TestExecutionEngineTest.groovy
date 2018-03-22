package com.github.h2020_5gtango.vnv.tee.engine

import com.github.mrduguo.spring.test.AbstractSpec
import com.github.h2020_5gtango.vnv.tee.restmock.TestResultRepositoryMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class TestExecutionEngineTest extends AbstractSpec {

    @Autowired
    TestResultRepositoryMock testResultRepositoryMock

    void "bash test should executed with success status"() {
        given:
        testResultRepositoryMock.reset()

        when:
        def entity = postForEntity('/tng-vnv-tee/api/v1/test-suite-results', [
                test_plan_id              : 'test_plan_id',
                network_service_instance_id: 'network_service_instance_id',
                test_suite_id             : 'test_suite_id',
        ], Map.class)

        then:
        entity.statusCode == HttpStatus.OK
        entity.body.status == 'SUCCESS'
        testResultRepositoryMock.testSuiteResults.size()==1
        testResultRepositoryMock.testSuiteResults.values().first().status=='SUCCESS'
    }

    void "ttcn3 test should executed with failed status"() {
        given:
        testResultRepositoryMock.reset()

        when:
        def entity = postForEntity('/tng-vnv-tee/api/v1/test-suite-results', [
                test_plan_id              : 'test_plan_id',
                network_service_instance_id: 'network_service_instance_id',
                test_suite_id             : 'ttcn3',
        ], Map.class)

        then:
        entity.statusCode == HttpStatus.OK
        entity.body.status == 'FAILED'
        testResultRepositoryMock.testSuiteResults.size()==1
        testResultRepositoryMock.testSuiteResults.values().first().status=='FAILED'
    }

}
