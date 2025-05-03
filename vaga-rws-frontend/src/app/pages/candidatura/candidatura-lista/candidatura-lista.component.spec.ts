import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CandidaturaListaComponent } from './candidatura-lista.component';

describe('CandidaturaListaComponent', () => {
  let component: CandidaturaListaComponent;
  let fixture: ComponentFixture<CandidaturaListaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CandidaturaListaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CandidaturaListaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
