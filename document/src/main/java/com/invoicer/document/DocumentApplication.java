package com.invoicer.document;

import io.github.classgraph.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DocumentApplication {

    public static void main(String[] args) throws IOException {
        File file = new File("variables.csv");
        FileWriter writer = new FileWriter(file);
        try (ScanResult scanResult = new ClassGraph().acceptPackages("com.invoicer").enableAllInfo().scan()) {
            for (ClassInfo classInfo : scanResult.getAllClasses().filter(new ClassInfoList.ClassInfoFilter() {
                @Override
                public boolean accept(ClassInfo classInfo) {
                    return !classInfo.getPackageName().equals("com.invoicer.document");
                }
            })) {
                if (classInfo.getDeclaredFieldInfo().isEmpty()) {
                    continue;
                }
                writer.write(classInfo.getName() + "\n");
                writer.write("Name, Data type, Scope, Purpose\n");
                for (FieldInfo fieldInfo : classInfo.getDeclaredFieldInfo()) {
                    writer.write(fieldInfo.getName() + "," + fieldInfo.getTypeDescriptor().toStringWithSimpleNames() + ",");
                    writer.write((fieldInfo.isPublic() ? "Public" : "Private") + " ");
                    writer.write((fieldInfo.isStatic() ? "Static" : "Non-static") + " ");
                    writer.write((fieldInfo.isFinal() ? "Final" : "Non-final") + " ");
                    writer.write("\n");
                }
            }
        }
        writer.close();
    }

}
