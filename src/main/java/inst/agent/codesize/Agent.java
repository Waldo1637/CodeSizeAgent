package inst.agent.codesize;

import java.lang.instrument.Instrumentation;

/**
 * Computes the size of each loaded class.
 *
 * @author Timothy Hoffman
 */
public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        // register NonTransformer for every class that will be loaded in the future
        inst.addTransformer(NonTransformer.INSTANCE, true);

        // call NonTransformer.transform for each class already loaded
        for (Class<?> c : inst.getAllLoadedClasses()) {
            if (inst.isModifiableClass(c)) {
                inst.retransformClasses(c);
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                NonTransformer.INSTANCE.printResults();
            }
        });
    }

    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("Code Size Checker " + Agent.class.getPackage().getImplementationVersion());
        System.out.println("==============================================================");
        System.out.println("Use as a java agent (listed after other java agents) to ");
        System.out.println("compute the size (in bytes) of every loaded class and print");
        System.out.println("to: " + ClassSizeLog.OUTPUT_FILE);
        System.out.println("==============================================================");
    }
}
