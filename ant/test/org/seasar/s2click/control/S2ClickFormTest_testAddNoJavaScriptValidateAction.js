<script type="text/javascript"><!--
function validate_form_text() {
   var msg = validateTextField(
         'form_text',true, 0, 0, ['Text は必須入力です。','Text は 0 文字以上で入力してください。','Text は 0 文字以内で入力してください。']);
   if (msg) {
      return msg + '|form_text';
   } else {
      return null;
   }
}
function on_form_submit() {
  var actionName = document.form.action.value;
  var noValidateActions = ["submit"];
  var skipValidation = false;
  for(var i=0;i<noValidateActions.length;i++){
    if(actionName == noValidateActions[i]){
      skipValidation = true;
      break;
    }
  }
  if(skipValidation == false){
    var msgs = new Array(1);
    msgs[0] = validate_form_text();
    if(!validateForm(msgs, 'form', 'left', null)){
      return false;
    }
  }
  return true;
}
//--></script>
