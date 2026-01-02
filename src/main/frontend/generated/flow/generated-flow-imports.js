import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === 'd5afb161eeb49eaf25b4e4d94246446e34d98190cbd7e271e0b0ea8249b0b22e') {
    pending.push(import('./chunks/chunk-6b0a7225bde9e3641e925fbd52561a9b9442684d18fe78ca48efff372354750a.js'));
  }
  if (key === '8daeaa2f48cec46b3294a11a0efe9fb66b2a4ed464af7dfd3c4a9177914f2866') {
    pending.push(import('./chunks/chunk-da0374f292d17e8f4fd8f219826fe806e49a81ded9bf86ebc5b5b87dc872137a.js'));
  }
  if (key === '52bdb91adf93a9bbf672b858d4a127085ae8e5fcd35fce57449a533d67096b78') {
    pending.push(import('./chunks/chunk-0620f6d4ff7b56dd6b8268212025edbcca2067d489b83822cb070203f3be0424.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}