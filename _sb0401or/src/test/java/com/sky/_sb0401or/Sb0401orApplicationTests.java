package com.sky._sb0401or;

import com.sky._sb0401or.entity.Memo;
import com.sky._sb0401or.repository.MemoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.stream.IntStream;

@SpringBootTest
class Sb0401orApplicationTests {

    @Autowired
    MemoRepository memoRepository;
    @Test
    void contextLoads() {
        System.out.println(memoRepository.getClass().getName());
    }

//    @Test
//    @DisplayName("메모입력테스트")
//    void inertMemo(){
//        Memo memo = Memo.builder()
//               .memoText("테스트ㅁ")
//                .build();
//        memoRepository.save(memo);
//    }

//@Test
//@DisplayName("메모입력테스트")
//void inertMemos(){
//    IntStream.rangeClosed(1,10).forEach(i->{
//        System.out.println(i);
//    });
//    Memo memo = Memo.builder()
//            .memoText("테스트ㅁ")
//            .build();
////    memoRepository.save(memo);
//}

@Test
@DisplayName("메모입력테스트")
void insertMemos() {
    Memo memo = Memo.builder()
            .memoText("테스트ㅁ"+i)
            .build();
    memoRepository.save(memo);
}


    @Test
//    @Test
    @Transactional
    void setMemoRepository(){
    Long mno = 1L;
    Memo memo memoRepository.getOne(mno);
        System.out.println(memo);
    }

}
