package com.example.elearningplatform.course.course;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.elearningplatform.cloudinary.CloudinaryService;
import com.example.elearningplatform.course.course.dto.AddInstructorRequest;
import com.example.elearningplatform.course.course.dto.CreateCourseRequest;
import com.example.elearningplatform.course.course.dto.UpdateCourseRequest;
import com.example.elearningplatform.exception.CustomException;
import com.example.elearningplatform.response.CoursesResponse;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.validator.Validator;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private CourseRepository courseRepository;


    /*******************************************************************************************/
    @GetMapping("/public/search/{searchKey}/{pageNumber}")
    public CoursesResponse searchCourse(@PathVariable("searchKey") String searchKey,
            @PathVariable("pageNumber") Integer pageNumber) throws SQLException {

        return courseService.findBysearchkey(searchKey, pageNumber);
    }


    /*******************************************************************************************/
    @GetMapping("/public/get-by-category/{categoryName}/{pageNumber}")
    public CoursesResponse searchCourseWithCategory(@PathVariable("categoryName") String categoryName,
            @PathVariable("pageNumber") Integer pageNumber) throws SQLException {
        return courseService.getCoursesByCategoryName(categoryName, pageNumber);
    }

    /*******************************************************************************************/
    @GetMapping("/public/get-by-tag/{tagName}/{pageNumber}")
    public CoursesResponse searchCourseWithTag(@PathVariable("tagName") String tagName,
            @PathVariable("pageNumber") Integer pageNumber)
            throws SQLException {
        return courseService.getCoursesByTagName(tagName, pageNumber);
    }

    /*******************************************************************************************/

    @GetMapping("/public/get-course/{id}")
    @SecurityRequirement(name = "bearerAuth")
    //ممكن تديها توكن وممك  لا 
    public Response getCourse(@PathVariable("id") Integer id)
            throws SQLException {
        return courseService.getCourse(id);
    }

    /***************************************************************************************** */
    @GetMapping("/public/get-courses/{pageNumber}")
    public CoursesResponse getAllCourses(@PathVariable("pageNumber") Integer pageNumber) {
        return courseService.getAllCourses(pageNumber);
    }

    /**
     * @throws InterruptedException
     * @throws IOException
     ***************************************************************************************/
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/create-course")
    public Response createCourse(@RequestBody @Valid CreateCourseRequest course, BindingResult result)
            throws IOException, InterruptedException {

      if (result.hasErrors()) {
            return Validator.validate(result);
        }
        return courseService.createCourse(course);
    }

    /***********************************************************************************************************/
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/update-course")
    public Response updateCourse(@RequestBody @Valid UpdateCourseRequest course, BindingResult result)
            throws IOException, InterruptedException {
                if (result.hasErrors()) {
                    return Validator.validate(result);
                }
        return courseService.updateCourse(course);
    }

    /************************************************************************************************************/
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-course")
    public Response unPublishCourse(@RequestParam("id") Integer id) throws SQLException {

        return courseService.unPublishCourse(id);
    }
    
    /************************************************************************************************************/

    // @GetMapping("/display-image/{id}")
    // public Response displayImage(@PathVariable("id") Integer id) throws
    // SQLException {
    // Course course = courseRepository.findById(id).orElse(null);
    // if (course == null) {
    // return new Response(HttpStatus.NOT_FOUND, "Course not found", null);
    // }
    // return new Response(HttpStatus.OK, "Success", course.getImage());

    // }

    /*************************************************************************************************** */
    
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/upload-image")
    @ResponseBody
    public ResponseEntity<String> upload(@RequestParam MultipartFile image, @RequestParam("courseId") int courseId)
            throws IOException {
        try {
            BufferedImage bi = ImageIO.read(image.getInputStream());
            if (bi == null) {
                throw new CustomException("Invalid image file", HttpStatus.BAD_REQUEST);
            }
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
            if (course.getImageId() != null) {
                cloudinaryService.delete(course.getImageId());
            }
            @SuppressWarnings("rawtypes")
            Map result = cloudinaryService.upload(image);
            course.setImageId((String) result.get("public_id"));
            course.setImageUrl((String) result.get("url"));
            courseRepository.save(course);

            return new ResponseEntity<>("image uploaded  ! ", HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload image to Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /******************************************************************************************** */

    
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-image")
    public ResponseEntity<String> deleteImage(@RequestParam("courseId") int courseId) {
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
            String cloudinaryImageId = course.getImageId();
            try {
                cloudinaryService.delete(cloudinaryImageId);
            } catch (IOException e) {
                throw new CustomException("Failed to delete image from Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            course.setImageId(null);
            courseRepository.save(course);
            return new ResponseEntity<>("image deleted !", HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete image from Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /******************************************************************************************** */
    
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("publish-course/{id}")
    public Response publishCourse(@PathVariable("id") Integer id) throws SQLException {
        return courseService.publishCourse(id);
    }

    /******************************************************************************************** */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add-instructor")
    public Response addInstructor(@RequestBody AddInstructorRequest request) {

        return courseService.addInstructor(request);
    }

    /******************************************************************************************** */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/delete-instructor")
    public Response deleteInstructor(@RequestBody AddInstructorRequest request) {

        return courseService.deleteInstructor(request);
    }
}
