import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VagaSearchComponent } from './vaga-search.component';

describe('VagaSearchComponent', () => {
  let component: VagaSearchComponent;
  let fixture: ComponentFixture<VagaSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VagaSearchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VagaSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
