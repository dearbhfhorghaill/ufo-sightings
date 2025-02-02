package org.ufo.sightings.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ufo.sightings.entities.Sighting;
import org.ufo.sightings.repositories.SightingRepository;

import java.time.LocalDateTime;

@RestController // This means that this class is a Controller
@RequestMapping(path="/ufo-sightings") // This means URLs start with /ufo-sightings (after Application path)
public class MainController {

    @Autowired // This means to get the bean called sightingRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private SightingRepository sightingRepository;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewSighting (@RequestParam LocalDateTime datetime
            , @RequestParam String comments) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Sighting s = new Sighting();
        s.setDatetime(datetime);
        s.setComments(comments);
        sightingRepository.save(s);
        //best practice is to response with HttpStatus.CREATED, and include URI to newly created
        //object in Header -> Location
        return "Saved";
    }

//    @PostMapping
//    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb, Principal principal) {
//        CashCard cashCardWithOwner = new CashCard(null, newCashCardRequest.amount(), principal.getName());
//        CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);
//        URI locationOfNewCashCard = ucb
//                .path("cashcards/{id}")
//                .buildAndExpand(savedCashCard.id())
//                .toUri();
//        return ResponseEntity.created(locationOfNewCashCard).build();
//    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Sighting> getAllSightings() {
        // This returns a JSON or XML with the sightings
        return sightingRepository.findAll();
    }

//    @GetMapping("/{requestedId}")
//    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal) {
//        CashCard cashCard = findCashCard(requestedId, principal);
//        if (cashCard != null) {
//            return ResponseEntity.ok(cashCard);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    @GetMapping
//    private ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {
//        Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
//                PageRequest.of(
//                        pageable.getPageNumber(),
//                        pageable.getPageSize(),
//                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
//                ));
//        return ResponseEntity.ok(page.getContent());
//    }

//    @PutMapping("/{requestedId}")
//    private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCard cashCardUpdate, Principal principal) {
//        CashCard cashCard = findCashCard(requestedId, principal);
//        if (cashCard != null) {
//            CashCard updatedCashCard = new CashCard(requestedId, cashCardUpdate.amount(), principal.getName());
//            cashCardRepository.save(updatedCashCard);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//
    //TODO - decide if implementing hard or soft delete i.e. move data to archive, set some flag/deleted timestamp instead of actually removing record etc.
//    @DeleteMapping("/{id}")
//    private ResponseEntity<Void> deleteCashCard(@PathVariable Long id, Principal principal) {
//        if (cashCardRepository.existsByIdAndOwner(id, principal.getName())) {
//            cashCardRepository.deleteById(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    private CashCard findCashCard(Long requestedId, Principal principal) {
//        return cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
//    }
}
