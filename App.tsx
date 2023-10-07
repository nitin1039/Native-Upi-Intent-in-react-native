import { View, Text, NativeModules, Pressable, ToastAndroid, DeviceEventEmitter } from 'react-native'
import React, { useEffect, useState } from 'react'
import Toast from 'react-native-toast-message';

const App = () => {
  const [paymentStatus, setPaymentStatus] = useState(null);

  const { GetPayment } = NativeModules

  const showMessageHndler = () => {

    GetPayment.initiateUpiPayment('bha******r****.3*****41@hdfcbank', '1.00', (error, message) => {
      if (error) {
        ToastAndroid.show(error, ToastAndroid.LONG);
      } else {
        ToastAndroid.show(message, ToastAndroid.LONG);
      }
    });

  }

  useEffect(() => {
    const subscription = DeviceEventEmitter.addListener('onUpiPaymentComplete', (event) => {
        setPaymentStatus(event.status);

        if (event.status === 'success') {
            // Show toast only when payment is successful
            ToastAndroid.show("Payment Completed Successfully.", ToastAndroid.LONG);
        } else if (event.status === 'cancelled') {
            // Toast for payment cancellation
            ToastAndroid.show(event.message, ToastAndroid.LONG);
        } else if(event.status==='failure'){
          ToastAndroid.show(event.message, ToastAndroid.LONG);
        }
    });

    
    return () => {
        subscription.remove();
    };
}, []);


  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
    <Pressable onPress={() => {
      showMessageHndler()
    }} style={{ backgroundColor:'blue',padding: 20,paddingHorizontal: 50,borderRadius: 9 }} >
     

      
      <Text style={{color: 'white', fontWeight: 'bold',fontSize:20}}>pay now</Text>
     
    </Pressable>
    {paymentStatus && <Text>Payment Status: {paymentStatus}</Text>}


    </View>
  )
}

export default App