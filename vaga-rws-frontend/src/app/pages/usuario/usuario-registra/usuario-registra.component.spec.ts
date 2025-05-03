import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsuarioRegistraComponent } from './usuario-registra.component';

describe('UsuarioRegistraComponent', () => {
  let component: UsuarioRegistraComponent;
  let fixture: ComponentFixture<UsuarioRegistraComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsuarioRegistraComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsuarioRegistraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
