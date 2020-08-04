package inst.agent.codesize;

/*-
 * #%L
 * CodeSizeAgent
 * %%
 * Copyright (C) 2020 Timothy Hoffman
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
