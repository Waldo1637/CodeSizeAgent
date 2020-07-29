package inst.agent.codesize;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author Timothy Hoffman
 */
public class NonTransformer implements ClassFileTransformer {

    private static final String PKGNAME = NonTransformer.class.getPackage().getName().replace('.', '/');

    public static NonTransformer INSTANCE = new NonTransformer();

    private final ClassSizeLog log;

    private NonTransformer() {
        log = new ClassSizeLog();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!className.startsWith(PKGNAME)) {   //ignore classes in the current package
            log.log(className, classfileBuffer.length);
        }
        return null;
    }

    public void printResults() {
        log.printToFile();
    }
}
