## JobParameter & Scope
* JobParameter 
    * Spring Batch 내부 혹은 외부에서 파라미터를 받아 Batch 컴포넌트에서 사용할 수 있게 해주는 파라미터.
    * Job Parameter 를 사용하기 위해 Spring Batch 전용 Scope을 선언해야 한다. 
        * Spring 에서는 Singleton을 주로 쓰고, Prototype과 같은 Scope이 존재함. 
    * Spring Batch에서 사용하는 Scope은 주로 StepScope, JobScope이 존재.
* JobScope는 Step 선언문에서 사용, StepScope는 Tasklet이나, ItemReader, ItemWriter, ItemProcessor에서 사용가

## StepScope와 JobScope
* Spring Batch는 StepScope와 JobScope라는 Scope을 지원.
* Spring Batch의 컴포넌트에 StepScope를 사용하면 빈 생성 시점이 기능을 실행하는 순간이 된다. (ProtoType ?)
* JobParamter는 StepScope이나 JobScope의 컴포넌트가 생성되는 순간에만 정상적으로 바인딩된다 .
