package com.java.jkpot.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.java.jkpot.model.SectionalMocks;

public interface SectionalMockRepository extends MongoRepository<SectionalMocks, Long>{

	@Query(fields = "{'answer': 0}")
	List<SectionalMocks> findSectionalMockByExamIdAndSectionalIdAndSubSectionalId(int examId, int sectionalId, int subSectionalId);

	List<SectionalMocks> findByExamIdAndSectionalIdAndSubSectionalId(int examId, int sectionalId, int subSectionalId);

	SectionalMocks findByExamIdAndSectionalIdAndSubSectionalIdAndSectionQuestionNo(int examId, int sectionalId, int subSectionalId, int questionNo);

}
