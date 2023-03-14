package com.hobam.vote.controllers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.hobam.vote.models.AllUsers;
import com.hobam.vote.models.Candidats;
import com.hobam.vote.models.ElectionListe;
import com.hobam.vote.models.Votants;
import com.hobam.vote.models.Form.ElectionForm;
import com.hobam.vote.models.Form.UserForm;
import com.hobam.vote.repositories.CandidatsRepository;
import com.hobam.vote.repositories.VotantsRepository;
import com.hobam.vote.services.CustomUserDetailsService;
import com.hobam.vote.services.ElectionListeService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
//@PreAuthorize("hasRole('ROLE_USER')")
@Secured("ROLE_ADMIN")
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private ElectionListeService election;
	
	@Autowired
	private CustomUserDetailsService service;
	
	
//    private AuthenticationManager authenticationManager;
	
	@Autowired
	private CandidatsRepository candRep;
	
	@Autowired
	private VotantsRepository voteRep;
	
	public Collection<? extends GrantedAuthority> check(HttpServletRequest httpServletRequest, HttpSession session) {
		String name = (String) session.getAttribute("USER");
		//System.out.println("\n\n\nIN\n\n\n"+name);
    	UserDetails userDetails = service.loadUserByUsername(name);
    	
    	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());   // HERE IS THE getAuthorities function
        usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	
		return auth.getAuthorities();
		
	}
	
	@GetMapping("/dashboard")
	public String dashAdmin(HttpServletRequest httpServletRequest, HttpSession session, Model model) {
		
		if (!this.check(httpServletRequest, session).stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        	
			model.addAttribute("erreur", "Veuillez vous reconnecter !");
        	return "error";
        }
		
		List<ElectionListe> elecListe = election.read();
		LocalDateTime myDateObj = LocalDateTime.now();
		model.addAttribute("date", myDateObj);
		
//		WebContext ctx = new WebContext(null, null, null);
//	    ctx.setVariable("prods", elecListe);
//
//	    templateEngine.process("product/list", ctx, response.getWriter());
	    model.addAttribute("elections", elecListe);
	    return "/admin/template_admin/listElection";
	}
	
	@GetMapping("/new/election")
	public String newElection(HttpServletRequest httpServletRequest, HttpSession session, Model model) {
		
		if (!this.check(httpServletRequest, session).stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        	
			model.addAttribute("erreur", "Veuillez vous reconnecter !");
        	return "error";
        }
		
		model.addAttribute("date", new String());
		
		model.addAttribute("election", new ElectionForm());
		return "/admin/template_admin/createElection";
	}
	
	@PostMapping("/new/election")
	public String storeElection(@ModelAttribute ElectionForm electionF, @RequestParam("file") MultipartFile excel, @RequestParam("file2") MultipartFile excel2, HttpServletRequest httpServletRequest, HttpSession session, Model model) {
//		
		if (!this.check(httpServletRequest, session).stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        	
			model.addAttribute("erreur", "Veuillez vous reconnecter !");
        	return "error";
        }
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		
		
//		System.out.println("\n\n\nrrr");
		ElectionListe electionR = new ElectionListe();
		try {
			System.out.println(electionF.getDate());
			
			LocalDateTime dateTime = LocalDateTime.parse(electionF.getDate(), formatter);
		    electionR.setCloture(dateTime);
		} catch (Exception e) {
			e.printStackTrace();
		    return "redirect:/admin/new/election";
		}
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
		
	    electionR.setCode(generatedString);
		
		electionR.setElectionName(electionF.getElectionName());
		electionR.setDescription(electionF.getDescription());
		electionR.setState(0);
		Random rand = new Random();
		int numb = rand.nextInt(6)+1;
		String image = "election"+String.valueOf(numb)+".jpeg";
		electionR.setImage(image);
		
		ElectionListe elecSaved = election.save(electionR);
		try {
			
			//fichier des candidats
			XSSFWorkbook workbook = new XSSFWorkbook(excel.getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			for(int i=1; i<sheet.getPhysicalNumberOfRows();i++) {
				XSSFRow row = sheet.getRow(i);
				String[] tab= new String[7];
				for(int j=0;j<row.getPhysicalNumberOfCells();j++) {
					tab[j] = String.valueOf(row.getCell(j));
					//System.out.print(row.getCell(j) +" ");
				}
				Candidats cand = new Candidats();
				cand.setNom(tab[0]);
				cand.setPrenom(tab[1]);
				cand.setGenre(tab[2]);
				cand.setNaissance(tab[3]);
				cand.setPhone(tab[4]);
				cand.setEmail(tab[5]);
				cand.setDescription(tab[6]);
				cand.setElectionid(elecSaved.getId());
				cand.setVoix(0);
				candRep.save(cand); 
				System.out.println("");
			}
			
			//fichier des votants
			XSSFWorkbook workbook2 = new XSSFWorkbook(excel2.getInputStream());
			XSSFSheet sheet2 = workbook2.getSheetAt(0);
			
			for(int i=1; i<sheet2.getPhysicalNumberOfRows();i++) {
				XSSFRow row = sheet2.getRow(i);
				String[] tab= new String[7];
				for(int j=0;j<row.getPhysicalNumberOfCells()-1;j++) {
					tab[j] = String.valueOf(row.getCell(j));
					//System.out.print(row.getCell(j) +" ");
				}
				Votants vot = new Votants();
				vot.setNom(tab[0]);
				vot.setPrenom(tab[1]);
				vot.setMatricule(tab[2]);
				
				vot.setGenre(tab[3]);
				vot.setNaissance(tab[4]);
				vot.setPhone(tab[5]);
				vot.setEmail(tab[6]);
				vot.setHaveVote(0);
				vot.setElectionid(elecSaved.getId());
				voteRep.save(vot); 
				System.out.println("");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "redirect:/admin/new/election";
		}
		return "redirect:/admin/dashboard";
	}
	
	@GetMapping("/election/{id}")
	public String showElection(@PathVariable("id") final Long id, Model model, HttpServletRequest httpServletRequest, HttpSession session) {
		
		if (!this.check(httpServletRequest, session).stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        	
			model.addAttribute("erreur", "Veuillez vous reconnecter !");
        	return "error";
        }
		
		model.addAttribute("id", id);
		if (election.find(id).isPresent())
			model.addAttribute("code", election.find(id).get().getCode());
		return "/admin/template_admin/index";
	}
	
	@GetMapping("/liste/candidats/{id}")
	public String showCandidats(@PathVariable("id") final Long id, Model model, HttpServletRequest httpServletRequest, HttpSession session) {
		
		if (!this.check(httpServletRequest, session).stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        	
			model.addAttribute("erreur", "Veuillez vous reconnecter !");
        	return "error";
        }
		
		List<Candidats> candidats = candRep.findAllByElectionid(id);
		model.addAttribute("candidats", candidats);
		return "/admin/template_admin/listCandidats";
	}
	
	@GetMapping("/liste/candidats/classement/{id}")
	public String showClassement(@PathVariable("id") final Long id, Model model, HttpServletRequest httpServletRequest, HttpSession session) {
		
		if (!this.check(httpServletRequest, session).stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        	
			model.addAttribute("erreur", "Veuillez vous reconnecter !");
        	return "error";
        }
		List<Candidats> first = candRep.findByElectionidOrderByVoixDesc(id);
	  	Iterator<Candidats> it = first.iterator();
	  	
	  	//System.out.println("\n\n\nINt\n\n\n"+ it.next().getDescription());
	  	model.addAttribute("first", first);
	  	int sum = 0;
	  	for (int i = 0; i<first.size(); i++) {
	  		//System.out.println("\n\n\nINt\n\n\n"+ it.next().getVoix());
	  		sum+= it.next().getVoix();
	  	}
	  	model.addAttribute("total", sum);
	  	model.addAttribute("election", election.find(id).get());
	  	
		return "/admin/template_admin/classement";
	}
	
	@GetMapping("/election/delete/{id}")
	public String deleteElection(@PathVariable("id") final Long id, HttpServletRequest httpServletRequest, HttpSession session, Model model) {
		
		if (!this.check(httpServletRequest, session).stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        	
			model.addAttribute("erreur", "Veuillez vous reconnecter !");
        	return "error";
        }
		
		System.out.println("\n\n\n"+election.find(id).isPresent());
		election.updateState(id, 1);
		return "redirect:/admin/dashboard";
	}
	
	@GetMapping("/election/votant/{id}")
	public String showVotants(@PathVariable("id") final Long id, HttpServletRequest httpServletRequest, HttpSession session,Model model) {
		
		if (!this.check(httpServletRequest, session).stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
        	
			model.addAttribute("erreur", "Veuillez vous reconnecter !");
        	return "error";
        }
		
		List<Votants> vots = voteRep.findAllByElectionid(id);
		model.addAttribute("votants", vots);
		
		return "/admin/template_admin/listeVotants";
	}
	
	
	
  	
  	
}
