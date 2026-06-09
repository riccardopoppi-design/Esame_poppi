package com.riccardopoppi.catalogo_cap.controllers;

import com.riccardopoppi.catalogo_cap.domain.Cappello;
import com.riccardopoppi.catalogo_cap.services.CappelloService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Sort;

@Controller
public class CappelloController {

    @Autowired
    private CappelloService cappelloService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "sort", required = false, defaultValue = "nome") String sortField) {

        List<Cappello> cappelli;

        Sort ordinamento = Sort.by(sortField).ascending();

        // Ricerca per nome
        if (search != null && !search.trim().isEmpty()) {
            cappelli = cappelloService.findByNomeContainingIgnoreCase(search, ordinamento);
        } else {
            cappelli = cappelloService.findAll(ordinamento);
        }
        model.addAttribute("cappelli", cappelli);
        model.addAttribute("search", search);
        model.addAttribute("sort", sortField);

        return "index";
    }

    @GetMapping("/new")
    public String formNuovoCappello(Model model) {
        model.addAttribute("cappello", new Cappello());
        model.addAttribute("taglie", List.of("XS", "S", "M", "L", "XL", "Unica"));
        return "form-cappello";
    }

    @GetMapping("/svuota")
    public String svuotaCatalogo() {
        cappelloService.deleteAll();
        return "redirect:/";
    }

    @PostMapping("/new")
    public String salvaCappello(@ModelAttribute Cappello cappello, 
                                @RequestParam("file") org.springframework.web.multipart.MultipartFile file, 
                                RedirectAttributes flash) {
        
        if (!file.isEmpty()) {
            try {
                String nomeFile = cappelloService.saveImage(file);
                cappello.setImmagine(nomeFile);
            } catch (Exception e) {
                flash.addFlashAttribute("error", "Errore nel caricamento dell'immagine");
            }
        }

        Cappello salvato = cappelloService.save(cappello);
        flash.addFlashAttribute("success", "Cappello inserito con successo!");
        return "redirect:/item/" + salvato.getId();
    }

    @GetMapping("/item/{id}")
    public String dettaglioCappello(@PathVariable UUID id, Model model) {
        Optional<Cappello> opt = cappelloService.findById(id);
        
        if (opt.isPresent()) {
            model.addAttribute("cappello", opt.get());
            return "dettaglio-cappello";
        }
        
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String eliminaCappello(@PathVariable UUID id, RedirectAttributes flash) {
        cappelloService.deleteById(id);
        flash.addFlashAttribute("deleted", "Cappello rimosso correttamente dal catalogo.");
        
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String formModificaCappello(@PathVariable UUID id, Model model) {
        Optional<Cappello> opt = cappelloService.findById(id);
        
        if (opt.isPresent()) {
            model.addAttribute("cappello", opt.get());
            model.addAttribute("taglie", List.of("XS", "S", "M", "L", "XL", "Unica"));
            return "edit-cappello";
        }
        
        return "redirect:/";
    }

    @PostMapping("/edit/{id}")
    public String aggiornaCappello(@PathVariable UUID id, 
                                @ModelAttribute Cappello cappello, 
                                @RequestParam("file") MultipartFile file, // Riceve il file anche qui
                                RedirectAttributes flash) {
        try {
            cappello.setId(id);
            
            if (!file.isEmpty()) {
                String nomeImmagine = cappelloService.saveImage(file);
                cappello.setImmagine(nomeImmagine);
            } else {
                // Se non carichiamo una nuova foto, dobbiamo recuperare quella vecchia
                // altrimenti verrebbe sovrascritta con null nel database
                cappelloService.findById(id).ifPresent(old -> cappello.setImmagine(old.getImmagine()));
            }

            Cappello aggiornato = cappelloService.save(cappello);
            flash.addFlashAttribute("success", "Cappello aggiornato con successo!");
            return "redirect:/item/" + aggiornato.getId();
            
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Errore nell'aggiornamento: " + e.getMessage());
            return "redirect:/edit/" + id;
        }
    }
}