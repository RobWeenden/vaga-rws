import { AbstractControlOptions, FormGroup, ValidationErrors, ValidatorFn } from "@angular/forms";

export const senhasConferemValidator: ValidatorFn = (control: AbstractControlOptions): ValidationErrors | null => {
  const form = control as FormGroup;
  const senha = form.get('senha')?.value;
  const confirmarSenha = form.get('confirmarSenha')?.value;
  if (senha && confirmarSenha) {
    return senha === confirmarSenha ? null : { senhasNaoConferem: true };
  }
  return null;
}
