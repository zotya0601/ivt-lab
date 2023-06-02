package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimary, mockSecondary;  

  @BeforeEach
  public void init(){
    mockPrimary = mock(TorpedoStore.class); 
    mockSecondary = mock(TorpedoStore.class); 
    this.ship = new GT4500(mockPrimary, mockSecondary);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    /* 
      Teszteljük, hogy amennyiben a másodlagos TorpedoStore üres, de az elsődleges még nem, 
      valamint a FALIURE_RATE-ből adódó meghibásodás sem lépett fel, akkor valóban TRUE értékkel tér-e vissza a fireTorpedo függvény
    */ 
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(true);  
    when(mockPrimary.fire(1)).thenReturn(true); 
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_SingleSecondary_Success(){
    /* 
      Teszteljük, hogy amennyiben a másodlagos TorpedoStore üres, de az elsődleges még nem, 
      valamint a FALIURE_RATE-ből adódó meghibásodás sem lépett fel, akkor valóban TRUE értékkel tér-e vissza a fireTorpedo függvény
    */ 
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);  
    when(mockSecondary.fire(1)).thenReturn(true); 
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(mockSecondary, times(1)).fire(1); 
  }

  /* 
    További 5 teszteset a fireTorpedo függvényhez:
      - Először az elsődleges TorpedoStore kell tüzeljen (első lövéskor csak az)
      - Második lövéskor a secondary TorpedoStore kell lőjön (mind a kettő egyszer lőtt ekkor)
      - Legelső lövéskor, ha a primary üres, akkor a secondary-t használja
      - Ha az egyik üres, akkor 2 lövés esetén egyből lő kétszer
      - Ha az egyik TorpedoStore hibát jelzett, nem próbálkozik a másikkal
  */ 

  @Test 
  public void fireTorpedo_primaryShootsFirst_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);  
    when(mockPrimary.fire(1)).thenReturn(true); 
    when(mockSecondary.fire(1)).thenReturn(true);
    
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    
    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(0)).fire(1);    
  } 

  @Test 
  public void fireTorpedo_shootTwice_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);  
    when(mockPrimary.fire(1)).thenReturn(true); 
    when(mockSecondary.fire(1)).thenReturn(true);
    
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = ship.fireTorpedo(FiringMode.SINGLE);
    
    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).fire(1);    
  } 

  @Test 
  public void fireTorpedo_primaryEmptyShootSecondaryFirst_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);  
    when(mockPrimary.fire(1)).thenReturn(false); 
    when(mockSecondary.fire(1)).thenReturn(true);
    
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    
    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(1)).fire(1);    
  } 

  @Test 
  public void fireTorpedo_primaryEmptyShootSecondaryTwice_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);  
    when(mockPrimary.fire(1)).thenReturn(false); 
    when(mockSecondary.fire(1)).thenReturn(true);
    
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = ship.fireTorpedo(FiringMode.SINGLE);
    
    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(2)).fire(1);    
  } 

  @Test 
  public void fireTorpedo_Malfunction_Success(){
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(true);
    
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    
    assertEquals(false, result);
    
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(0)).fire(1);  
  } 

  @Test 
  public void fireTorpedo_TestBasedOnReadingTheCode_Success(){
    // Minden üres 
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(true);
    when(mockPrimary.fire(1)).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(false);
    
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    
    assertEquals(false, result);
    
    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
  } 

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);      
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).fire(1); 
  }

  @Test 
  public void fireTorpedo_AllBothEmpty_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(0)).fire(1); 
  } 

}
