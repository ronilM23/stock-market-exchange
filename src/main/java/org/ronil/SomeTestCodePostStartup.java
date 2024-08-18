package org.ronil;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Profile("temp")
@Component
public class SomeTestCodePostStartup implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        testExecutors();
    }

    public static void testExecutors() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<SomeTask> someTasks = new ArrayList<>();
        someTasks.add(new SomeTask("abc"));
        someTasks.add(new SomeTask("def"));
        someTasks.add(new SomeTask("xyz"));

//        Future future = executorService.submit(someTasks.get(0));
//        future.get();
//        System.out.println(future.isDone());
        List<Future<String>> futureList = executorService.invokeAll(someTasks);
        executorService.shutdown();
        futureList.stream().forEach(f -> {
            try {
                System.out.println(f.isDone() + f.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static class SomeTask implements Callable<String> {

        private String input;

        public SomeTask(String input) {
            this.input = input;
        }

        @Override
        public String call() {
            return "Processed: " + input;
        }
    }
}
