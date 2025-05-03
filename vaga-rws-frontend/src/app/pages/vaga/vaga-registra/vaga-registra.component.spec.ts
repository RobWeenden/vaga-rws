import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VagaRegistraComponent } from './vaga-registra.component';

describe('VagaRegistraComponent', () => {
  let component: VagaRegistraComponent;
  let fixture: ComponentFixture<VagaRegistraComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VagaRegistraComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VagaRegistraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
