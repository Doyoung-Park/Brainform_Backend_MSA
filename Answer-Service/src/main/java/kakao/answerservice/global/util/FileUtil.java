package kakao.answerservice.global.util;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Getter
@Component
public class FileUtil {


    @Value("${ec2.absolute.path}")
    private String absolutePath;

    public static String brainWaveFolderPath;


    @PostConstruct
    public void init() {
        brainWaveFolderPath = absolutePath + "src//main//resources//static//images//brainwave//";
    }

    // uuid 추가한 이미지 이름 반환
    private String getUniqueImageName(MultipartFile file) {
        return UUID.randomUUID() + "_" + file.getOriginalFilename();
    }


    // 유저 이미지를 저장할 경로 반환
    private Path getBrainWaveImagePath(String imageName) {
        return Paths.get(brainWaveFolderPath + imageName);
    }

    public String saveUserImage(MultipartFile file) {
        String uniqueImageName = getUniqueImageName(file);

        Path filePath = getBrainWaveImagePath(uniqueImageName);

        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uniqueImageName;
    }

    public void deleteUserImage(String imageName) {
        File file = new File(brainWaveFolderPath + imageName);
        if (file.exists()) {
            file.delete();
        }
    }

}

