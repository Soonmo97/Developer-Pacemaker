package project.developer_pacemaker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.developer_pacemaker.dto.gpt.GptControlDTO;
import project.developer_pacemaker.dto.gpt.GptDTO;
import project.developer_pacemaker.entity.GptEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.GptRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GptService {

    final private GptRepository gptRepository;

    public GptService(GptRepository gptRepository) {
        this.gptRepository = gptRepository;
    }

    public List<GptControlDTO> getGptListByUSeq(long uSeq) {
        List<GptEntity> gptEntities = gptRepository.findByUser_uSeqAndIsDeleted(uSeq, false);
        return gptEntities.stream()
                .map(gpt -> new GptControlDTO(gpt.getGSeq(),gpt.getQuestion(), gpt.getAnswer()))
                .collect(Collectors.toList());
    }

    public void saveGpt(long uSeq, GptDTO gptDto) {
        GptEntity gptEntity = new GptEntity();
        gptEntity.setQuestion(gptDto.getQuestion());
        gptEntity.setAnswer(gptDto.getAnswer());

        UserEntity userEntity = new UserEntity();
        userEntity.setUSeq(uSeq);
        gptEntity.setUser(userEntity);

        gptRepository.save(gptEntity);
    }


    public boolean deleteGptByGSeq(long gSeq) {

        try{
            GptEntity gptEntity = gptRepository.findById(gSeq)
                    .orElseThrow(() -> new RuntimeException("Gpt data with gSeq " + gSeq + " not found"));

            gptEntity.setDeleted(true);
            gptRepository.save(gptEntity);
            return true;

        }catch (Exception e){
            return false;
        }

    }

    public List<GptDTO> getDeletedGptListByUser(long uSeq) {
        List<GptEntity> gptEntities = gptRepository.findByUser_uSeqAndIsDeleted(uSeq, true);
        return gptEntities.stream()
                .map(gpt -> new GptDTO(gpt.getQuestion(), gpt.getAnswer()))
                .collect(Collectors.toList());
    }
}
