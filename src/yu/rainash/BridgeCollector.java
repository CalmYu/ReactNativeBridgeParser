package yu.rainash;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import yu.rainash.bridge.BridgeMethod;
import yu.rainash.bridge.BridgeModule;
import yu.rainash.bridge.ReactNativePackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BridgeCollector {

    private static final String ANNOTATION_REF_REACT_MODULE = "Lcom/facebook/react/module/annotations/ReactModule;";
    private static final String ANNOTATION_REF_REACT_METHOD = "Lcom/facebook/react/bridge/ReactMethod;";

    private ReactNativePackage ReactNativePackage = new ReactNativePackage();

    public ReactNativePackage collectBridgeInfo(File classesJar) throws IOException {
        collectBridgeFromJar(classesJar);
        return ReactNativePackage;
    }

    public void collectBridgeFromJar(File jarFile) throws IOException {
        File unJarDir = new File(jarFile.getParent(), "__classes__");
        JarUtils.unJar(jarFile, unJarDir);
        collectBridgeFromDirectory(unJarDir);
        FileUtils.deleteDirectory(unJarDir);
    }

    public void collectBridgeFromDirectory(File dir) throws IOException {
        Collection<File> classFiles = FileUtils.listFiles(dir, new String[]{"class"}, true);
        for (File classFile : classFiles) {
            collectBridgeFromClassFile(classFile);
        }
    }

    public void collectBridgeFromClassFile(File classFile) throws IOException {
        ClassReader reader = new ClassReader(new FileInputStream(classFile));
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.SKIP_DEBUG);

        List<AnnotationNode> classAnnotations = classNode.visibleAnnotations;
        if (!Utils.isEmptyCollection(classAnnotations)) {
            for (AnnotationNode annotation : classAnnotations) {
                if (ANNOTATION_REF_REACT_MODULE.equals(annotation.desc)) {
                    // 只对rn module的class 做处理
                    BridgeModule bridgeModule = new BridgeModule();
                    String moduleName = (String) getAnnotationValue(annotation, "name");
                    if (moduleName == null) {
                        continue;
                    }
                    bridgeModule.moduleName = moduleName;
                    bridgeModule.className = classNode.name.replace("/", ".");
                    bridgeModule.bridgeMethods = new ArrayList<>();
                    List<MethodNode> methodNodes = classNode.methods;
                    if (methodNodes != null && methodNodes.size() > 0) {
                        for (MethodNode method : methodNodes) {
                            List<AnnotationNode> methodAnnotations = method.visibleAnnotations;
                            if (!Utils.isEmptyCollection(methodAnnotations)) {
                                for (AnnotationNode methodAnnotation : methodAnnotations) {
                                    if (ANNOTATION_REF_REACT_METHOD.equals(methodAnnotation.desc)) {
                                        // react method
                                        BridgeMethod bridgeMethod = new BridgeMethod();
                                        bridgeMethod.methodName = method.name;
                                        bridgeMethod.returnType = Type.getReturnType(method.desc).getClassName();
                                        Type[] argumentTypes = Type.getArgumentTypes(method.desc);
                                        List<String> argumentTypeClassNames = new ArrayList<>();
                                        for (Type argumentType : argumentTypes) {
                                            argumentTypeClassNames.add(argumentType.getClassName());
                                        }
                                        bridgeMethod.argumentTypes = argumentTypeClassNames;
                                        bridgeModule.bridgeMethods.add(bridgeMethod);
                                    }
                                }
                            }
                        }
                    }
                    ReactNativePackage.addModule(bridgeModule);
                }
            }
        }
    }

    private static Object getAnnotationValue(AnnotationNode annotation, String queryKey) {
        for (int x = 0; x < annotation.values.size() - 1; x += 2) {
            final Object key = annotation.values.get(x);
            final Object value = annotation.values.get(x + 1);
            if (key instanceof String && key.equals(queryKey)) {
                return value;
            }
        }
        return null;
    }

}