package com.sky._sb0401;

import com.sky._sb0401.entity.Board;
import com.sky._sb0401.entity.Member;
import com.sky._sb0401.entity.Memo;
import com.sky._sb0401.repository.MemoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
class Sb0401ApplicationTests {

    @Autowired
    MemoRepository memoRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    void m1Test() {
        Member member = Member.builder().name("홍길동").password("1234").role("사용자").userId("hong").build();
        em.persist(member);
        Board board = Board.builder().cnt(0L).title("처음 제목").content("처음 글 내용").member(member).build();
        em.persist(board);

    }


    @Test
    void emTest() {
        Memo memo = em.find(Memo.class, 2L);
        System.out.println(memo);
    }

    @Test
    void emTest2() {
        List<Memo> list
                = em.createQuery("from Memo m where m.mno > 5", Memo.class)
                                                   .getResultList();
        list.stream().forEach(e->{
            System.out.println(e);
        });
    }

    @Test
    @Transactional
    void emInsert() {
        Memo memo = Memo.builder()
                .memoText("이것은 EM 테스트")
                .build();
        em.persist(memo);

    }

    @Test
    void contextLoads() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    @DisplayName("메모 입력 테스트")
    void inertMemo() {
        Memo memo = Memo.builder()
                .memoText("이것은 테스트")
                .build();
        memoRepository.save(memo);
    }

    @Test
    void inertMemos() {
        IntStream.rangeClosed(1, 10).forEach(i->{
            Memo memo = Memo.builder()
                    .memoText("이것은 테스트..."+i)
                    .build();
            memoRepository.save(memo);
        });
    }

    @Test
    @Transactional
    void selectMemos() {
       Long mno = 10L;
        Memo memo = memoRepository.getOne(mno);
        System.out.println(memo);
    }

    @Test
    @Transactional
    void testProcedure() {
        for (Memo memo : memoRepository.getMemo()) {
            System.out.println(memo);
        }
    }

}
