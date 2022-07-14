package ch.usi.si.bsc.sa4.devinecodemy.model.user;

import ch.usi.si.bsc.sa4.devinecodemy.controller.dto.user.SocialMediaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SocialMediaTests {
    SocialMedia socialMedia;
    SocialMedia socialMedia2;

    @BeforeEach
    void beforeAllTests() {
        socialMedia = new SocialMedia("a Twitter", "a Skype", "a linkedin");
        socialMedia2 = new SocialMedia("another Twitter", "another Skype", "another linkedin");
    }

    @Test
    @DisplayName("Constructor Social Media")
    public void testConstructor(){
        assertEquals("a Twitter", socialMedia.getTwitter(), "Twitter is not set correctly");
        assertEquals("a Skype", socialMedia.getSkype(), "Skype is not set correctly");
        assertEquals("a linkedin", socialMedia.getLinkedin(), "Linkedin is not set correctly");
    }

    @Test
    @DisplayName("Twitter getter")
    public void testGetTwitter(){
        assertEquals("a Twitter", socialMedia.getTwitter(), "Twitter is not set correctly");
        assertEquals("another Twitter", socialMedia2.getTwitter(), "Twitter is not set correctly");
    }

    @Test
    @DisplayName("Skype getter")
    public void testGetSkype(){
        assertEquals("a Skype", socialMedia.getSkype(), "Skype is not set correctly");
        assertEquals("another Skype", socialMedia2.getSkype(), "Skype is not set correctly");
    }

    @Test
    @DisplayName("Linkedin getter")
    public void testGetLinkedin(){
        assertEquals("a linkedin", socialMedia.getLinkedin(), "Linkedin is not set correctly");
        assertEquals("another linkedin", socialMedia2.getLinkedin(), "Linkedin is not set correctly");
    }

    @Test
    @DisplayName("Sending to Social Media DTO")
    public void testToSocialMediaDTO() {
        SocialMediaDTO socialMediaDTO = socialMedia.toSocialMediaDTO();
        assertEquals("a Twitter", socialMediaDTO.getTwitter(), "Twitter is not set correctly");
        assertEquals("a Skype", socialMediaDTO.getSkype(), "Skype is not set correctly");
        assertEquals("a linkedin", socialMediaDTO.getLinkedin(), "Linkedin is not set correctly");
    }
}
