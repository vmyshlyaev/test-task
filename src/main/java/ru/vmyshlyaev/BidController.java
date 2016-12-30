package ru.vmyshlyaev;


import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class BidController {

    @Autowired
    private BidRepository repository;

    // create new bid
    @RequestMapping(path = "/add", method= RequestMethod.POST)
    public void addBid(@RequestParam String name,
                       @RequestParam(defaultValue="Initial") String status) {

        repository.save(new Bid(name, status));
    }

    //get bid status by Id
    @RequestMapping(path = "/get/{id}", method= RequestMethod.GET)
    public String getBidStatus(@PathVariable("id") String id) {

        Long bidId;

        try {
            bidId = Long.parseLong(id);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Incorrect Bid ID format";
        }

        if (repository.exists(bidId)) {

            Bid bid = repository.findOne(bidId);
            return "Bid name: " + bid.getName() + "    status: " + bid.getStatus();
        }

        return "Bid not found";

    }
}
