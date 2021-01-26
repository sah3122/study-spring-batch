package me.study.springbatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextConditionalJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    /**
     * BatchStatus는 Spring에서 배치 실행 결과를 기록하기 위하여 사용하는 값,
     * ExitStatus와는 다른 값이다. 
     */
    /**
     * Step의 상태값을 기준으로 실행 Flow를 분기 할 수 있다.
     *
     * on() : 캐치할 ExitStatus 지정, *는 모든 상태
     * to() : 다음으로 이동할 Step
     * from() : 일종의 이벤트 리스너, 상태값을 보고 일치한다면 to()에 포함된 step 호출,
     * step1의 이벤트 캐치가 failed로 되어 있는 상태에서 추가로 이벤트 캐치를 하려면 from이 필요하다.
     *
     * end() : flowBuilder를 반환하는 end, flowBuilder를 종료하는 end
     * on("*") 뒤에 end()는 flowBuilder를 반환
     * build() 앞 end()는 flowBuilder 종료
     * flowBuilder를 반환하는 end 뒤엔 from으로 flow를 계속 이어갈 수 있음.
    *
     */
    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStep1())
                    .on("FAILED") // falied 인 경우
                    .to(conditionalJobStep3()) // step 3 실행
                    .on("*") // step3의 status가 무엇이든
                    .end()// step3을 마지막으로 flow가 종료
                .from(conditionalJobStep1()) // step 1로 부터
                    .on("*") // failed 가 아닌 다른 status인 경우
                    .to(conditionalJobStep2()) // step 2로 이동
                    .next(conditionalJobStep3()) // step2가 정상인경우 step3 실행
                    .on("*") // step3의 status가 무엇이던
                    .end() // 종료한다.
                .end()
                .build();
    }

    @Bean
    public Step conditionalJobStep1() {
        return stepBuilderFactory.get("step1")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("stepNextConditionalJob Step1");
                    /***
                     * ExitStatus 를 바라보며 job 실행 조건이 결정된다.
                     */
                    stepContribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }


    @Bean
    public Step conditionalJobStep2() {
        return stepBuilderFactory.get("step2")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("stepNextConditionalJob Step2");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }


    @Bean
    public Step conditionalJobStep3() {
        return stepBuilderFactory.get("step3")
                .tasklet(((stepContribution, chunkContext) -> {
                    log.info("stepNextConditionalJob Step1");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }
}
