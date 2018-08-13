package com.spss.smarthome.qrcode;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.spss.smarthome.common.utils.qrcode.wrapper.QrCodeGenWrapper;
import com.spss.smarthome.common.utils.qrcode.wrapper.QrCodeOptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * 二维码图片生成测试
 */
@Slf4j
public class QRcodeTest {
    @Test
    public void generateQRcode() {
        String mac = "SPSS-00:0c:43:e1:76:28";
        Integer size = 500;
        File qrcodeImageFile = new File("images/gateway/qrcode/", UUID.randomUUID().toString() + ".png");

        try {

            BufferedImage img = QrCodeGenWrapper.of(mac)
                    .setW(size)
                    .setH(size)
//                    .setDrawPreColor(0xff008e59)
//                    .setDrawPreColor(0xff9C877B)        //二维码的颜色
                    .setErrorCorrection(ErrorCorrectionLevel.M)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)        //二维码的样式
                    .setLogoBgColor(Color.LIGHT_GRAY)
//                    .setLogo(logo)
                    .setLogoRate(10)
//                    .setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE)       //二维码信息的样式
                    .setDrawEnableScale(true)
                    .asBufferedImage();
            ImageIO.write(img, "png", qrcodeImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
        log.info(qrcodeImageFile.getAbsolutePath());
        assertTrue(qrcodeImageFile.isFile());
    }
}
