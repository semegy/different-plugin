package different.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

// 支持的注解
@SupportedAnnotationTypes("different.processor.Different")
public class DifferAssist extends AbstractProcessor {

    Set<String> diffCache = new HashSet<>();

    Set<String> circulate = new HashSet<>();

    Set<String> basicType = new HashSet<>();
    static String CHECK_DIFF = "import different.processor.CheckDiff;";

    static String CHECK_RESULT = "import different.processor.CheckResult;";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        basicType.addAll(Arrays.asList("long", "int", "short", "char", "boolean", "float", "double", "byte"));

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Different.class);
        elementsAnnotatedWith.forEach(element -> {
            if (element.getKind() == ElementKind.FIELD) {

                try {
                    Class<?> aClass = Class.forName(element.asType().toString());
                    extracted(aClass);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return true;
    }

    private void extracted(Class<?> targetClass) throws ClassNotFoundException, IOException {
        Field[] declaredFields = targetClass.getDeclaredFields();
        circulate.add(targetClass.getName());
        List<String> fieldNames = new ArrayList<>();
        Map<String, String> fileClassName = new HashMap<>();
        List<String> importPackage = new ArrayList<>();
        for (Field declaredField : declaredFields) {
            if (declaredField.getDeclaredAnnotation(Skip.class) == null) {
                String fieldName = declaredField.getName();
                if (basicType.contains(declaredField.getType().getTypeName()) || Arrays.stream(declaredField.getType().getInterfaces()).anyMatch((inter) -> inter.equals(Comparable.class))) {
                    fieldNames.add(fieldName);
                } else if (declaredField.getType().equals(Map.class) || Arrays.stream(declaredField.getType().getInterfaces()).anyMatch((inter) -> inter.equals(Map.class))) {
                    importPackage.add("import " + MapCheckDiff.class.getName() + ";");
                    fileClassName.put(fieldName, "MapCheckDiff");
                } else {
                    fileClassName.put(fieldName, declaredField.getType().getSimpleName() + "CheckDiff");
                    Class<?> type = declaredField.getType();
                    if (!type.getPackage().equals(targetClass.getPackage())) {
                        importPackage.add("import " + type.getName() + "CheckDiff;");
                    }
                    if (!circulate.contains(type.getName())) {
                        extracted(type);
                    }
                }
            }
        }
        if (!diffCache.contains(targetClass.getSimpleName())) {
            // 类名
            String name = targetClass.getSimpleName() + "CheckDiff";
            JavaFileObject builderFile = processingEnv.getFiler()
                    .createSourceFile(targetClass.getName() + "CheckDiff");
            try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                // 包名
                out.println(targetClass.getPackage() + ";");

                // import com.example.CheckDiff;
                out.println(CHECK_DIFF);
                out.println(CHECK_RESULT);
                importPackage.forEach(value -> out.println(value));

                // 类名
                out.println("public class " + name + " extends CheckDiff" + "<" + targetClass.getSimpleName() + "> {");

                // diff项初始化
                out.println("{");
                fieldNames.forEach(value -> out.println("checkDiffList.add(this::" + value + "Check);"));
                fileClassName.forEach((key, value) -> out.println("checkDiffList.add(this::" + key + "Check);"));
                out.println("}");

                // 构造函数
                out.println("public  " + name + "() { super(); }");
                out.println("public  " + name + "(CheckResult checkResult) { super(checkResult);}");
                out.println("public  " + name + "(CheckResult checkResult, String keyName) { super(checkResult, keyName); }");

                //diff方法

                fieldNames.forEach(value -> {
                    out.print("public boolean " + value + "Check()");
                    out.println("{");
                    out.print("return super.childDiff");
                    out.print("(");
                    out.print("t1." + value + ", ");
                    out.print("t2." + value + ", ");
                    out.print("this.keyName +" + "\"_" + value + "\"");
                    out.print(")");
                    out.println(";");
                    out.println("}");
                });
                fileClassName.forEach((key, value) -> {
                    out.print("public boolean " + key + "Check()");
                    out.println("{");
                    out.print(value + " checkDiff" + "= new " + value + "(this.checkResult, " + "this.keyName + " + "\"_" + key + "\"" + ");");
                    out.print("return checkDiff.diff(" + "t1." + key + ", " + "t2." + key + ").success();");
                    out.println("}");
                });
                // 结束
                out.println("}");
            }

        }
    }
}