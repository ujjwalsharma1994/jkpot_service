package com.java.jkpot.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.java.jkpot.api.response.pojo.RestResponse;
import com.java.jkpot.dao.CountersDAO;
import com.java.jkpot.model.ExamSyllabus;
import com.java.jkpot.model.MockExams;
import com.java.jkpot.model.SectionalMocks;
import com.poiji.bind.Poiji;

@RestController
@RequestMapping("/import")
public class SectionalMockXlsxToDBConverter {

	@Autowired
	private CountersDAO sequence;
	@Autowired
	private MongoTemplate mongoTemplate;

	@PostMapping(path = "/data/mcq/xlsx/{examId}/{sectionalId}/{subSectionalId}")
	public ResponseEntity<RestResponse> uploadExcelSectionalMock(@PathVariable(value = "sectionalId") int sectionalId,
			@PathVariable(value = "subSectionalId") int subSectionalId, @PathVariable(value = "examId") int examId,
			@RequestPart(value = "file") MultipartFile file) throws IOException {

		if (mongoTemplate.findOne(Query.query(Criteria.where("sectionalId").is(sectionalId))
				.addCriteria(Criteria.where("subSectionalId").is(subSectionalId))
				.addCriteria(Criteria.where("examId").is(examId)), SectionalMocks.class) != null) {

			RestResponse response = new RestResponse("FAILURE", "Data already inserted", 404);
			return ResponseEntity.ok(response);
		}

		else {
			String sectionalName = mongoTemplate
					.findOne(Query.query(Criteria.where("topicId").is(sectionalId)), ExamSyllabus.class).getTopic();

			File files = new File("jkpot/" + file.getOriginalFilename());
			files.createNewFile();
			FileOutputStream fout = new FileOutputStream(files);
			fout.write(file.getBytes());
			fout.close();

			List<MockExams> mockQuestions = Poiji.fromExcel(files, MockExams.class);
			ZipSecureFile.setMinInflateRatio(-1.0d);

			for (MockExams each : mockQuestions) {

				SectionalMocks mock = new SectionalMocks();

				List<String> options = new ArrayList<>();

				String question = each.getQuestion().trim();
				mock.setQuestion(question);

				options.add(each.getOptionA());
				options.add(each.getOptionB());
				options.add(each.getOptionC());
				options.add(each.getOptionD());

				mock.setOptions(options);
				mock.setQuestion(question);
				mock.setAnswer(each.getAnswer().trim());
				mock.setSectionName(sectionalName);
				mock.setExamId(examId);
				mock.setSectionQuestionNo(each.getSectionQuestionNo());
				mock.setSectionalMockId(sequence.getNextSequenceOfField("sectionalMockId"));
				if (each.getImageOption().equals("NO"))
					mock.setImageAdded(false);
				else
					mock.setImageAdded(true);
				mock.setSectionalId(sectionalId);
				mock.setSubSectionalId(subSectionalId);

				mongoTemplate.save(mock, "sectional_mocks");
			}

			RestResponse response = new RestResponse("SUCCESS", mockQuestions, 200);
			files.delete(); // delete the file
			return ResponseEntity.ok(response);
		}
	}
}