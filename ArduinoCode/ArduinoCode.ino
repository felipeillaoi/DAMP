int sensorPin = A3; 
int leitura;
void setup() {
  Serial.begin(9600); 
}
void loop() {
  leitura = analogRead(sensorPin); 
  Serial.print("Valor lido: "); 
  Serial.println(leitura); 
  delay(2000); 
}