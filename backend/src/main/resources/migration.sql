-- Migration script to add token_issue_date column to existing journey table
-- Run this script if you have an existing database

-- Add token_issue_date column if it doesn't exist
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'journey' AND column_name = 'token_issue_date') THEN
        ALTER TABLE journey ADD COLUMN token_issue_date DATE;
        
        -- Update existing records to set token_issue_date to created_at date
        UPDATE journey SET token_issue_date = DATE(created_at) WHERE token_issue_date IS NULL;
        
        -- Make the column NOT NULL after updating existing records
        ALTER TABLE journey ALTER COLUMN token_issue_date SET NOT NULL;
    END IF;
END $$;

-- Create indexes if they don't exist
CREATE INDEX IF NOT EXISTS idx_journey_token_issue_date ON journey(token_issue_date);
CREATE INDEX IF NOT EXISTS idx_journey_counter_date ON journey(counter_no, token_issue_date); 