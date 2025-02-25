import { FormGroup, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';


/**
* Update all controls of the provided form group with the given data.
*/
export function updateForm(group: FormGroup, data: any) {
  for (const field in group.controls) {
    const control = group.get(field)!;
    let value = data[field] === undefined ? null : data[field];
    control.setValue(value);
  }
}

export const validUuid: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const valid = control.value === null || /^[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}?$/.test(control.value);
  return valid ? null : { validUuid: { value: control.value } };
};

export const validOffsetDateTime: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const valid = control.value === null ||
      (/^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(\.[0-9]{1,6})?((\+[0-9]{2}:[0-9]{2})|Z)$/.test(control.value) &&
      !isNaN(Date.parse(control.value)));
  return valid ? null : { validOffsetDateTime: { value: control.value } };
};
