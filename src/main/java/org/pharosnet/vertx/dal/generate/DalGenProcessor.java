package org.pharosnet.vertx.dal.generate;

import com.alibaba.fastjson.JSON;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.pharosnet.vertx.dal.generate.def.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("org.pharosnet.vertx.dal.generate.VertxDAL")
@SupportedOptions({DalGenProcessor.option_out, DalGenProcessor.option_def})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class DalGenProcessor extends AbstractProcessor {

    static final String option_def = "codegen.define.dir";
    static final String option_out = "codegen.output";

    private String outPutParent;
    private String dir;
    private String abstractRow;
    private String execRow;

    private Configuration cfg;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        outPutParent = processingEnv.getOptions().getOrDefault(option_out, "");
        if (outPutParent.length() == 0) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "undefined " + option_out);
            return;
        }
        dir = processingEnv.getOptions().getOrDefault(option_def, "");
        if (dir.length() == 0) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "undefined " + option_def);
        }
        abstractRow = processingEnv.getOptions().getOrDefault("codegen.abstract.row", "");
        if (abstractRow.length() == 0) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "undefined codegen.abstract.row");
        }
        execRow = processingEnv.getOptions().getOrDefault("codegen.exec.row", "");
        if (execRow.length() == 0) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "undefined codegen.exec.row");
        }

        cfg = new Configuration(Configuration.getVersion());

        cfg.setClassLoaderForTemplateLoading(DalGenProcessor.class.getClassLoader(), "vertxdal");

        cfg.setDefaultEncoding("UTF-8");

        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);

    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(VertxDAL.class);
        try {
            Template template = cfg.getTemplate("row.ftl");
            FileUtils.forceMkdir(new File(outPutParent));
            for (Element element : elements) {
                String clazz = element.toString();
                String pkg = clazz.substring(0, clazz.lastIndexOf("."));
                VertxDAL vertxDAL = element.getAnnotation(VertxDAL.class);
                String def = vertxDAL.define().strip();
                String filePath = dir + "/" + def;
                String data = readFile(filePath);
                DataAccessLayer layer = JSON.parseObject(data, DataAccessLayer.class);
                this.writeTables(template, pkg, layer.getTables(), false);
                this.writeTables(template, pkg, layer.getViews(), true);
            }

        } catch (Throwable ex) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    private static String readFile(String filePath) throws Exception {
        return FileUtils.readFileToString(new File(filePath), Charset.forName("UTF-8"));
    }

    private void writeTables(Template template, String pkg, List<Table> tables, boolean view) throws Throwable {
        String pkgDir = outPutParent + "/" + pkg.replace(".", "/");
        FileUtils.forceMkdir(new File(pkgDir));
        for (Table table : tables) {
            String name = table.getName();
            String className = underlineToCamel(name, true) + "Row";

            TemplateData templateData = new TemplateData(view);
            templateData.setPkg(pkg);
            templateData.setClassName(className);
            templateData.setAbstractRow(abstractRow);
            templateData.setExecRow(execRow);
            templateData.setSchema(table.getSchema());
            templateData.setTableName(name);

            for (Column column : table.getColumns()) {
                String fieldName = underlineToCamel(column.getName(), false);
                Field field = new Field();
                field.setCol(column.getName());
                field.setName(fieldName);
                field.setDef(column.getDefaultValue());
                field.setType(column.getType());
                templateData.getFields().add(field);
            }

            templateData.init();

            Writer out = new FileWriter(new File(pkgDir + "/" + className + ".java"));

            template.process(templateData, out);

            out.close();

        }

    }

    private static String underlineToCamel(String param, boolean fu) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        param = param.toLowerCase();
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {

            char c = param.charAt(i);
            if (fu && i == 0) {
                c = Character.toUpperCase(c);
            }
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


}
