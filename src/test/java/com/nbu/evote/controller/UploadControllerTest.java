package com.nbu.evote.controller;

import com.nbu.evote.service.BallotService;
import com.nbu.evote.service.CitizenService;
import com.nbu.evote.service.PartyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UploadController.class)
@ActiveProfiles("test")
class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private PartyService partyService;

    @MockBean
    private CitizenService citizenService;

    @MockBean
    private BallotService ballotService;

    @BeforeEach
    void setUp() throws Exception {
        this.citizenFileUploader();
        this.partyFileUploader();
    }

    @Test
    void citizenFileUploader() throws Exception {
        RedirectAttributes redirectAttributes = null;
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return "src/main/resources/csv/lista_s_partii.csv";
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
        Model model = null;
        ResultActions resultActions = this.mockMvc.perform(post("/admin/upload/citizens",
                file, model, redirectAttributes))
                .andExpect(status().isFound());
    }

    @Test
    void partyFileUploader() {
    }
}