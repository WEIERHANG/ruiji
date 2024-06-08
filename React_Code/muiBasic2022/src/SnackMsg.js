import React from 'react';

import {Snackbar} from '@mui/material';
import  MuiAlert from '@mui/material/Alert';

const Alert = React.forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
  });

const SnackMsg = (props) => {
    return (
        <Snackbar 
            open={props.open} 
            anchorOrigin={{ vertical : 'bottom', horizontal : 'right' }}
            autoHideDuration={3000} 
            onClose={props.onClose} 
            message={props.message}>
            <Alert onClose={props.onClose} severity="success" sx={{ width: '100%' }}>
                {props.message}
            </Alert>
        </Snackbar>
    );
}

export default SnackMsg;
