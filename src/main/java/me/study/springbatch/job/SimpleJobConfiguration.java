package me.study.springbatch.job;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * Batch Job Execution 은 Batch Job Instance의 상태(성공, 실패)를 기록해 두는 테이블이라고 볼 수 있다.
     * Btach Job Instance는 동일한 Instacne가 생성되지 않는데, 요청이 실패한 경우 재 실행 가능하다.
     * 성공/ 실패 기록은 Batch Job Execution 테이블에 저장된다.
    * */
    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("SimpleJob")
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .build();
    }

    //
    /**
     * @JobScope 선언 후, jobParameters를 받는다.
     * 실행 파라미터로 requestDate=20210125 와 같이 전달. 동일한 값인 경우 Job Instance 가 생성되지 않는다.
     *  A job instance already exists and is complete for parameters={requestDate=20210125}.  If you want to run this job again, change the parameters.
     *
    * */

    @Bean
    @JobScope
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
