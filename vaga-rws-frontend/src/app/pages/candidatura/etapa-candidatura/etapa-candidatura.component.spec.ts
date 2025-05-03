import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EtapaCandidaturaComponent } from './etapa-candidatura.component';

describe('EtapaCandidaturaComponent', () => {
  let component: EtapaCandidaturaComponent;
  let fixture: ComponentFixture<EtapaCandidaturaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EtapaCandidaturaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EtapaCandidaturaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
