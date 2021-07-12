package com.ataraxia.microservices.util;

import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.NullInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lilong
 * 压缩包工具类
 * <p>
 * 1.如果时单个需要压缩的文件比较大的话可以使用bufferedInputStream 或者使用NIO管道流进行文件的读写提高压缩效率.
 * 2.如果是整体需要压缩的文件数量比较多的情况下那么则使用apache compress 提供的多线程的形式来提高压缩效率.
 * <p/>
 */
public final class ZipUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * @Return void
     * @Author Li Long
     * @Description 下载图片并且并且放入压缩包当中
     * @Date 15:59 2021/6/15
     * @Params [imageList, parallelScatterZipCreator]
     */
    public static void compressDownloadImage(List<String> imageList, ParallelScatterZipCreator parallelScatterZipCreator) {
        for (String item : imageList) {
            //获取图片名称
            String separator = "\\";
            if (item.contains("/")) {
                separator = "/";
            }
            String fileName = item.substring(item.lastIndexOf(separator) + 1);
            final InputStreamSupplier inputStreamSupplier = () -> {
                try {
                    BufferedImage bufferedImage = ImageIO.read(new URL(item));
                    //图片转为输入流
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                    return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                } catch (Exception e) {
                    e.printStackTrace();
                    return new NullInputStream(0);
                }
            };
            ZipArchiveEntry entry = new ZipArchiveEntry(fileName);
            entry.setMethod(ZipArchiveEntry.DEFLATED);
            parallelScatterZipCreator.addArchiveEntry(entry, inputStreamSupplier);
        }
    }

    /**
     * <b>将文件列表生成压缩包,生成的压缩包存储在系统路径。如果要将生成的压缩包向浏览器输出文件流的格式可以参考上面的图片压缩<b/>
     *
     * @param files   需要压缩的文件
     * @param zipFile 生成的压缩文件，文件类型必须是zip
     */
    public static boolean compressFile(List<File> files, File zipFile) throws IOException {
        InputStream inputStream = null;
        ZipArchiveOutputStream zipArchiveOutputStream = null;
        try {
            zipArchiveOutputStream = new ZipArchiveOutputStream(zipFile);
            for (File file : files) {
                //文件不存在则不做压缩操作
                if (!file.exists()) {
                    continue;
                }
                ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
                zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
                inputStream = new FileInputStream(file);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                IOUtils.copy(inputStream, outputStream);
                zipArchiveOutputStream.write(outputStream.toByteArray());
            }
            zipArchiveOutputStream.closeArchiveEntry();
            zipArchiveOutputStream.finish();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                Objects.requireNonNull(inputStream).close();
                Objects.requireNonNull(zipArchiveOutputStream).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean compressFile(File zipFile, List<StringWriter> writers) {
        ZipArchiveOutputStream zipArchiveOutputStream = null;
        try {
            zipArchiveOutputStream = new ZipArchiveOutputStream(zipFile);
            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("org/test/123.java");
            zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
            zipArchiveOutputStream.write(writers.get(0).toString().getBytes(StandardCharsets.UTF_8));
            zipArchiveOutputStream.closeArchiveEntry();
            zipArchiveOutputStream.finish();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                Objects.requireNonNull(zipArchiveOutputStream).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 生成压缩文件并写出
     *
     * @param zipFileName 压缩文件名称
     * @param response    响应
     * @param list        文件内容 【key值为文件名（使用/进行文件夹创建），value 压缩文件的内容】
     */
    public static void compressFileOutput(Map<String, StringWriter> list, String zipFileName, HttpServletResponse response) {
        if (!zipFileName.contains(".zip")) {
            zipFileName = zipFileName + ".zip";
        }
        //设置response参数
        response.reset();
        response.setContentType("content-type:octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(zipFileName.getBytes(), StandardCharsets.ISO_8859_1));

        ZipArchiveOutputStream zipArchiveOutputStream = null;
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            zipArchiveOutputStream = new ZipArchiveOutputStream(response.getOutputStream());
            for (Map.Entry<String, StringWriter> key : list.entrySet()) {
                ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(key.getKey());
                zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
                zipArchiveOutputStream.write(key.getValue().toString().getBytes(StandardCharsets.UTF_8));
            }
            zipArchiveOutputStream.closeArchiveEntry();
            zipArchiveOutputStream.finish();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert zipArchiveOutputStream != null;
                zipArchiveOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Return boolean
     * @Author Li Long
     * @Description 压缩包解压
     * @Date 14:12 2021/6/29
     * @Params [zipFile]
     */
    public static boolean unZip(File zipFile) {
        //文件解压需要一个地址，以及一个文件夹。那我们直接取压缩文件的解压地址与文件名即可
        String tempFile = null;
        ZipArchiveInputStream inputStream = null;
        if (!zipFile.exists()) {
            logger.error("压缩包地址不存在！");
            return false;
        }
        //保存压缩文件的文件夹
        tempFile = zipFile.getParent() + File.separator + zipFile.getName().split("\\.")[0];
        try {
            //创建最外层的文件夹
            Files.createDirectories(Paths.get(tempFile));
            inputStream = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
            ZipArchiveEntry zipArchiveEntry;
            while ((zipArchiveEntry = inputStream.getNextZipEntry()) != null) {
                if (zipArchiveEntry.isDirectory()) {
                    Files.createDirectories(Paths.get(tempFile, zipArchiveEntry.getName()));
                } else {
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(tempFile, zipArchiveEntry.getName())));
                    IOUtils.copy(inputStream, os);
                    os.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(inputStream).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public static void main(String[] args) throws IOException {

        StringWriter writer = new StringWriter();
        writer.write("测试信息");
        List<StringWriter> list = new ArrayList<>();
        list.add(writer);
        compressFile(new File("d:/123.zip"), list);
    }
}
