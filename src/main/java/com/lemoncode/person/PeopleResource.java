package com.lemoncode.person;

import com.lemoncode.file.ResponseMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

import static java.util.stream.Collectors.*;


@RestController
@RequestMapping(value = "/people")
public class PeopleResource {
    private final static Logger LOGGER = Logger.getLogger(PeopleResource.class.getName());

    @Autowired
    private PeopleService peopleService;

    @Autowired
    RelationshipLabelService labelService;

    @Autowired
    PersonMapper mapper;


    @PostMapping("/simple")
    public SimplePersonDTO createSimplePerson(@RequestBody SimplePersonDTO p) {
        return peopleService.createSimplePerson(p);
    }

    @PostMapping("/batch")
    public List<SimplePersonDTO> createPerson(@RequestBody String batch) {

        //validate each row
        String[] entries = batch.split("\n");

        List<Person> people = new ArrayList<>();
        for (String row : entries) {
            if (!PersonValidator.isValidBatchRow(row.trim())) {
                throw new BadRequestException(row + " does not match Pattern: John/Smith/M");
            }
            String[] arr = row.split("/");
            Person p = new Person();
            p.setFirstName(arr[0].trim());
            p.setLastName(arr[1].trim());
            p.setGender(GenderEnum.from(arr[2].trim()));
            people.add(p);
        }

        try {
            return peopleService.save(people);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Entry/Entries already existing in db");
        } catch (Exception e) {
            throw new RuntimeException("Server Error: " + e.getMessage());
        }
    }

    @PostMapping()
    public PersonDTO createPerson(@RequestBody PersonDTO p) {
        PersonDTO dto = peopleService.createPerson(p);

        if (CollectionUtils.isEmpty(p.getRelationships()) && CollectionUtils.isEmpty(p.getParents())) {
            return dto;
        }

        //  // create opposite associates from the Connections defined
        //    //e.g. if a ninong was defined, said niong should show inaanak for his profile.
        //    //if inaanak was defined, said inaanak will have either Ninong/Ninang defined depending on persons gender
        SimplePersonDTO main = this.mapper.toSimplePersonDTO(dto);
       peopleService.addMainInOppositeRelationship(main, p.getRelationships());
       peopleService.addMainAsChild(main, p.getParents());


        return dto;
    }


    @GetMapping
    public List<SimplePersonDTO> find(@RequestParam(value = "q", required = false) String query, @RequestParam(value = "exclude", required = false) String excludeIds) {

        if (query != null) {
            if (excludeIds != null && !excludeIds.trim().isEmpty()) {
                Set<Long> exclude = Arrays.stream(excludeIds.split(",")).map(Long::parseLong).collect(toSet());
                return peopleService.search(query, exclude);
            } else {
                return peopleService.search(query);
            }
        } else {
            if (excludeIds != null && !excludeIds.trim().isEmpty()) {
                Set<Long> exclude = Arrays.stream(excludeIds.split(",")).map(Long::parseLong).collect(toSet());
                return peopleService.findAllSimple(exclude);
            } else {
                return peopleService.findAllSimple();
            }
        }

    }

    @GetMapping("/recent")
    public List<SimplePersonDTO> getRecent() {
        return peopleService.getRecent();
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<ResponseMessage> saveImage(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            String name = peopleService.savePhoto(id, file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            ResponseMessage<String> res = new ResponseMessage<>(message);
            res.setData(name);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            e.printStackTrace();
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/simple/{id}")
    public SimplePersonDTO findSimpleById(@PathVariable("id") int id) {
        return peopleService.findOneSimple(id);
    }

    @GetMapping("/{id}")
    public PersonDTO findPersonById(@PathVariable("id") Long id) {
        return peopleService.findOne(id);
    }


    @GetMapping(
            value = "/image/{fileName}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImageWithMediaType(@PathVariable("fileName") String fileName) throws IOException {

        if (StringUtils.isEmpty(fileName))
            return new byte[]{};

        InputStream in = peopleService.getPhoto(fileName);
        return org.apache.commons.io.IOUtils.toByteArray(in);

    }


    @GetMapping("/labels")
    public Set<String> getLabels() {
        return labelService.getLabelsSet();
    }




}
