package ru.vmyshlyaev;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BidControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidRepository repository;

    @After
    public void clearBidRepository () {
        if (repository.count() > 0)
            repository.deleteAll();
    }

    // trying to correct get Bid status
    @Test
    public void correctGetIdShouldReturnCorrectMessage() throws Exception {

        Bid bid = new Bid("Test bid", "Initial");
        repository.save(bid);

        mockMvc.perform(get("/get/" + bid.getId())).andDo(print()).andExpect(status().isOk()).
                andExpect(content().string("Bid name: " + bid.getName() + "    status: " + bid.getStatus()));


    }

    // trying to get not existing bid
    @Test
    public void notExistingBidGetShouldReturnDefaultMessage() throws Exception {

        mockMvc.perform(get("/get/99")).andDo(print()).andExpect(status().isOk()).
                andExpect(content().string("Bid not found"));

    }

    // trying to get bid with incorrect format Id
    @Test
    public void notCorrectFormatIdGetShouldReturnDefaultMessage() throws Exception {

        mockMvc.perform(get("/get/1aa")).andDo(print()).andExpect(status().isOk()).
                andExpect(content().string("Incorrect Bid ID format"));

    }


    // add new Bid with params
    @Test
    public void withParamsShouldCreateNewBid() throws Exception {

        this.mockMvc.perform(post("/add").param("name", "Good").param("status", "Perfect"))
                .andDo(print()).andExpect(status().isOk());

        assertEquals(repository.findOne(1L).getName(),"Good" );
        assertEquals(repository.findOne(1L).getStatus(),"Perfect" );

    }

    // trying to add new default Bid without param Name
    @Test
    public void withoutParamsShouldNotCreateNewDefaultBid() throws Exception {

        this.mockMvc.perform(post("/add"))
                .andDo(print()).andExpect(status().is4xxClientError());


    }

}